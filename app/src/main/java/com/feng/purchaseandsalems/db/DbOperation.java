package com.feng.purchaseandsalems.db;

import com.feng.purchaseandsalems.constant.Constant;
import com.feng.purchaseandsalems.entity.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Feng Zhaohao
 * Created on 2019/12/6
 */
public class DbOperation {

    /**
     * 注册用户
     */
    public static void register(final UserData userData, final RegisterListener registerListener) {
        // 在子线程中进行
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection mainConnection = DbOpenHelper.getMainConnection();
                if (mainConnection == null) {
                    registerListener.registerError("连接数据库失败");
                    return;
                }
                // 创建用户
                String createSql = "CREATE USER ?@? IDENTIFIED BY ?";
                PreparedStatement ps = null;
                try {
                    ps = mainConnection.prepareStatement(createSql);
                    // 为 ? 设置具体的值
                    ps.setString(1, userData.getUserName());
                    ps.setString(2, "%");
                    ps.setString(3, userData.getPassword());
                    // 执行语句
                    ps.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    registerListener.registerError("执行 sql 语句失败，可能该用户已存在");
                    return;
                } finally {
                    if (ps != null) {
                        try {
                            ps.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
                // 根据用户类型，授予不同的权限
                switch (userData.getUserType()) {
                    case PURCHASE:
                        if (!grantPurchase(mainConnection, userData.getUserName())) {
                            registerListener.registerError("连接数据库失败");
                            return;
                        }
                        break;
                    case SALE:
                        if (!grantSale(mainConnection, userData.getUserName())) {
                            registerListener.registerError("连接数据库失败");
                            return;
                        }
                        break;
                    default:
                        break;
                }
                // 配置加密方式
                if (!alterPassword(mainConnection, userData)) {
                    registerListener.registerError("连接数据库失败");
                    return;
                }
                // 将信息写入 user 表
                if (!insertUser(mainConnection, userData)) {
                    registerListener.registerError("连接数据库失败");
                    return;
                }
                // 注册成功
                registerListener.registerSuccess();
            }
        }).start();

    }

    /**
     * 授予进货员权限
     */
    private static boolean grantPurchase(Connection connection, String userName) {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(
                    "GRANT SELECT, INSERT, UPDATE, DELETE ON purchase_and_sale.purchase TO ?@?");
            ps.setString(1, userName);
            ps.setString(2, "%");
            ps.executeUpdate();
            ps = connection.prepareStatement(
                    "GRANT SELECT, INSERT, UPDATE, DELETE ON purchase_and_sale.autoparts TO ?@?");
            ps.setString(1, userName);
            ps.setString(2, "%");
            ps.executeUpdate();
            ps = connection.prepareStatement(
                    "GRANT SELECT, INSERT, UPDATE, DELETE ON purchase_and_sale.staff TO ?@?");
            ps.setString(1, userName);
            ps.setString(2, "%");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return true;
    }

    /**
     * 授予销售员权限
     */
    private static boolean grantSale(Connection connection, String userName) {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(
                    "GRANT SELECT, INSERT, UPDATE, DELETE ON purchase_and_sale.sale TO ?@?");
            ps.setString(1, userName);
            ps.setString(2, "%");
            ps.executeUpdate();
            ps = connection.prepareStatement(
                    "GRANT SELECT, INSERT, UPDATE, DELETE ON purchase_and_sale.autoparts TO ?@?");
            ps.setString(1, userName);
            ps.setString(2, "%");
            ps.executeUpdate();
            ps = connection.prepareStatement(
                    "GRANT SELECT, INSERT, UPDATE, DELETE ON purchase_and_sale.staff TO ?@?");
            ps.setString(1, userName);
            ps.setString(2, "%");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return true;
    }

    /**
     * 配置加密方式
     */
    private static boolean alterPassword(Connection connection, UserData userData) {
        String sql = " ALTER USER ?@? IDENTIFIED WITH mysql_native_password BY ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, userData.getUserName());
            ps.setString(2, "%");
            ps.setString(3, userData.getPassword());
            // 执行语句
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return true;
    }

    /**
     * 将用户信息写入 user 表
     */
    private static boolean insertUser(Connection connection, UserData userData) {
        String sql = "insert into user (name, type) values (?, ?)";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, userData.getUserName());
            switch (userData.getUserType()) {
                case PURCHASE:
                    ps.setString(2, Constant.USER_TYPE_PURCHASE);
                    break;
                case SALE:
                    ps.setString(2, Constant.USER_TYPE_SALE);
                    break;
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return true;
    }

    /**
     * 获取用户类型
     */
    public static String getUserType(Connection connection, String userName) {
        String type = "";
        // 查询的 sql 语句
        String sql = "select * from user where name = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, userName);
            // 执行语句（执行查询语句用的是 executeQuery 方法）
            rs = ps.executeQuery();
            // 得到查询结果
            if (rs != null) {
                while (rs.next()) {
                    type = rs.getString("type");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return type;
    }
}
