package com.feng.purchaseandsalems.db;

import com.feng.purchaseandsalems.entity.AutopartsData;
import com.feng.purchaseandsalems.entity.StaffData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Feng Zhaohao
 * Created on 2019/12/9
 */
public class StaffOperation {

    /**
     * 查询是否存在某 id 的记录
     */
    static boolean isExistId(int id) {
        // 查询的 sql 语句
        String sql = "select * from staff where id = ?";
        Connection connection = DbOpenHelper.getUserConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean res = false;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            // 得到查询结果
            if (rs != null) {
                while (rs.next()) {
                    res = true;
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

        return res;
    }

    /**
     * 插入员工信息
     */
    public static void insertStaff(final StaffData staffData, final OperationListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 插入数据的 sql 语句
                String sql = "INSERT INTO `purchase_and_sale`.`staff`(`id`, `name`, `contact`)" +
                        " VALUES (?, ?, ?)";
                Connection connection = DbOpenHelper.getUserConnection();
                PreparedStatement ps = null;
                if (connection == null) {
                    listener.error("用户不存在，请重新登录");
                    return;
                }
                try {
                    ps = connection.prepareStatement(sql);
                    ps.setInt(1, staffData.getId());
                    ps.setString(2, staffData.getName());
                    ps.setString(3, staffData.getContact());
                    // 执行语句
                    ps.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    listener.error("数据库连接失败");
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

                listener.success();
            }
        }).start();
    }
}
