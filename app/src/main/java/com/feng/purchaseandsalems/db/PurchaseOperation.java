package com.feng.purchaseandsalems.db;

import android.util.Log;

import com.feng.purchaseandsalems.entity.PurchaseData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/12/8
 */
public class PurchaseOperation {

    /**
     * 查询是否存在某 id 的记录
     */
    public static boolean isExistId(int id) {
        // 查询的 sql 语句
        String sql = "select * from purchase where id = ?";
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
     * 插入进货信息
     */
    public static void insertPurchase(final PurchaseData purchaseData, final OperationListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 先检查是否存在该汽车配件
                if (!AutopartsOperation.isExistId(purchaseData.getAutoPartsId())) {
                    listener.error("不存在该汽车配件，请先插入该汽车配件的信息");
                    return;
                }
                // 再检查是否存在该员工
                if (!StaffOperation.isExistId(purchaseData.getStaffId())) {
                    listener.error("不存在该员工，请先插入该汽车配件的信息");
                    return;
                }

                // 插入数据的 sql 语句
                String sql = "insert into purchase (autoPartsId, num, factoryName, " +
                        "factoryAddress, factoryContact, staffId) values (?, ?, ?, ?, ?, ?)";
                Connection connection = DbOpenHelper.getUserConnection();
                PreparedStatement ps = null;
                if (connection == null) {
                    listener.error("用户不存在，请重新登录");
                    return;
                }
                try {
                    ps = connection.prepareStatement(sql);
                    ps.setInt(1, purchaseData.getAutoPartsId());
                    ps.setInt(2, purchaseData.getNum());
                    ps.setString(3, purchaseData.getFactoryName());
                    ps.setString(4, purchaseData.getFactoryAddress());
                    ps.setString(5, purchaseData.getFactoryContact());
                    ps.setInt(6, purchaseData.getStaffId());
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

    /**
     * 根据 id 删除进货信息
     */
    public static void deletePurchase(final int deleteId, final OperationListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!isExistId(deleteId)) {
                    listener.error("不存在该编号的进货信息");
                    return;
                }

                // 删除数据的 sql 语句
                String sql = "delete from purchase where id = ?";
                Connection connection = DbOpenHelper.getUserConnection();
                if (connection == null) {
                    listener.error("用户信息失效，请重新登录");
                    return;
                }
                PreparedStatement ps = null;
                try {
                    ps = connection.prepareStatement(sql);
                    ps.setInt(1, deleteId);
                    // 执行语句
                    ps.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    listener.error("执行 SQL 语句失败");
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

    /**
     * 更新进货信息
     */
    public static void alterPurchase(final PurchaseData purchaseData, final OperationListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 先检查是否存在该汽车配件
                if (!AutopartsOperation.isExistId(purchaseData.getAutoPartsId())) {
                    listener.error("不存在该汽车配件，请先插入该汽车配件的信息");
                    return;
                }
                // 再检查是否存在该员工
                if (!StaffOperation.isExistId(purchaseData.getStaffId())) {
                    listener.error("不存在该员工，请先插入该汽车配件的信息");
                    return;
                }

                deletePurchase(purchaseData.getId(), new OperationListener() {
                    @Override
                    public void success() {
                        // 插入数据的 sql 语句
                        String sql = "insert into purchase (id, autoPartsId, num, factoryName, " +
                                "factoryAddress, factoryContact, staffId) values (?, ?, ?, ?, ?, ?, ?)";
                        Connection connection = DbOpenHelper.getUserConnection();
                        PreparedStatement ps = null;
                        if (connection == null) {
                            listener.error("用户不存在，请重新登录");
                            return;
                        }
                        try {
                            ps = connection.prepareStatement(sql);
                            ps.setInt(1, purchaseData.getId());
                            ps.setInt(2, purchaseData.getAutoPartsId());
                            ps.setInt(3, purchaseData.getNum());
                            ps.setString(4, purchaseData.getFactoryName());
                            ps.setString(5, purchaseData.getFactoryAddress());
                            ps.setString(6, purchaseData.getFactoryContact());
                            ps.setInt(7, purchaseData.getStaffId());
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

                    @Override
                    public void error(String errorMsg) {
                        listener.error(errorMsg);
                    }
                });
            }
        }).start();
    }

    public interface QueryAllListener {
        void success(List<PurchaseData> dataList);
        void error(String errorMsg);
    }

    public interface QueryOneListener {
        void success(List<PurchaseData> dataList);
        void error(String errorMsg);
    }

    /**
     * 查询所有进货信息
     */
    public static void queryAll(final QueryAllListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 查询的 sql 语句
                String sql = "select * from purchase";
                Connection connection = DbOpenHelper.getUserConnection();
                if (connection == null) {
                    listener.error("用户信息失效，请重新登录");
                    return;
                }
                PreparedStatement ps = null;
                ResultSet rs = null;
                List<PurchaseData> dataList = new ArrayList<>();
                try {
                    ps = connection.prepareStatement(sql);
                    // 执行语句（执行查询语句用的是 executeQuery 方法）
                    rs = ps.executeQuery();
                    // 得到查询结果
                    if (rs != null) {
                        while (rs.next()) {
                            PurchaseData data = new PurchaseData();
                            data.setId(rs.getInt("id"));
                            data.setAutoPartsId(rs.getInt("autoPartsId"));
                            data.setNum(rs.getInt("num"));
                            data.setFactoryName(rs.getString("factoryName"));
                            data.setFactoryAddress(rs.getString("factoryAddress"));
                            data.setFactoryContact(rs.getString("factoryContact"));
                            data.setStaffId(rs.getInt("staffId"));
                            dataList.add(data);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    listener.error("执行 sql 语句失败");
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

                listener.success(dataList);
            }
        }).start();
    }

    /**
     * 根据汽车配件编号，查询指定的进货信息
     */
    public static void queryOne(final int autopartsId, final QueryOneListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 查询的 sql 语句
                String sql = "select * from purchase where autoPartsId = ?";
                Connection connection = DbOpenHelper.getUserConnection();
                if (connection == null) {
                    listener.error("用户信息失效，请重新登录");
                    return;
                }
                PreparedStatement ps = null;
                ResultSet rs = null;
                List<PurchaseData> dataList = new ArrayList<>();
                try {
                    ps = connection.prepareStatement(sql);
                    ps.setInt(1, autopartsId);
                    // 执行语句（执行查询语句用的是 executeQuery 方法）
                    rs = ps.executeQuery();
                    // 得到查询结果
                    if (rs != null) {
                        while (rs.next()) {
                            PurchaseData data = new PurchaseData();
                            data.setId(rs.getInt("id"));
                            data.setAutoPartsId(rs.getInt("autoPartsId"));
                            data.setNum(rs.getInt("num"));
                            data.setFactoryName(rs.getString("factoryName"));
                            data.setFactoryAddress(rs.getString("factoryAddress"));
                            data.setFactoryContact(rs.getString("factoryContact"));
                            data.setStaffId(rs.getInt("staffId"));
                            dataList.add(data);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    listener.error("执行 sql 语句失败");
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

                listener.success(dataList);
            }
        }).start();
    }
}
