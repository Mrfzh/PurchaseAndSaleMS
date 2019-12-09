package com.feng.purchaseandsalems.db;

import com.feng.purchaseandsalems.entity.PurchaseData;
import com.feng.purchaseandsalems.entity.SaleData;

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
public class SaleOperation {

    /**
     * 查询是否存在某 id 的记录
     */
    public static boolean isExistId(int id) {
        // 查询的 sql 语句
        String sql = "select * from sale where id = ?";
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
    public static void insertSale(final SaleData saleData, final OperationListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 先检查是否存在该汽车配件
                if (!AutopartsOperation.isExistId(saleData.getAutoPartsId())) {
                    listener.error("不存在该汽车配件，请先插入该汽车配件的信息");
                    return;
                }
                // 再检查是否存在该员工
                if (!StaffOperation.isExistId(saleData.getStaffId())) {
                    listener.error("不存在该员工，请先插入该汽车配件的信息");
                    return;
                }

                // 插入数据的 sql 语句
                String sql = "insert into sale (autoPartsId, num, customerName, " +
                        "customerContact,  staffId) values (?, ?, ?, ?, ?)";
                Connection connection = DbOpenHelper.getUserConnection();
                PreparedStatement ps = null;
                if (connection == null) {
                    listener.error("用户不存在，请重新登录");
                    return;
                }
                try {
                    ps = connection.prepareStatement(sql);
                    ps.setInt(1, saleData.getAutoPartsId());
                    ps.setInt(2, saleData.getNum());
                    ps.setString(3, saleData.getCustomerName());
                    ps.setString(4, saleData.getCustomerContact());
                    ps.setInt(5, saleData.getStaffId());
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
    public static void deleteSale(final int deleteId, final OperationListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!isExistId(deleteId)) {
                    listener.error("不存在该编号的进货信息");
                    return;
                }

                // 删除数据的 sql 语句
                String sql = "delete from sale where id = ?";
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
    public static void alterSale(final SaleData saleData, final OperationListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 先检查是否存在该汽车配件
                if (!AutopartsOperation.isExistId(saleData.getAutoPartsId())) {
                    listener.error("不存在该汽车配件，请先插入该汽车配件的信息");
                    return;
                }
                // 再检查是否存在该员工
                if (!StaffOperation.isExistId(saleData.getStaffId())) {
                    listener.error("不存在该员工，请先插入该汽车配件的信息");
                    return;
                }

                deleteSale(saleData.getId(), new OperationListener() {
                    @Override
                    public void success() {
                        // 插入数据的 sql 语句
                        String sql = "insert into sale (id, autoPartsId, num, customerName, " +
                                "customerContact, staffId) values (?, ?, ?, ?, ?, ?)";
                        Connection connection = DbOpenHelper.getUserConnection();
                        PreparedStatement ps = null;
                        if (connection == null) {
                            listener.error("用户不存在，请重新登录");
                            return;
                        }
                        try {
                            ps = connection.prepareStatement(sql);
                            ps.setInt(1, saleData.getId());
                            ps.setInt(2, saleData.getAutoPartsId());
                            ps.setInt(3, saleData.getNum());
                            ps.setString(4, saleData.getCustomerName());
                            ps.setString(5, saleData.getCustomerContact());
                            ps.setInt(6, saleData.getStaffId());
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


    public interface QueryCountListener {
        void success(int count);
        void error(String errorMsg);
    }

    /**
     * 查询所有进货信息
     */
    public static void queryCount(final int autopartsId, final QueryCountListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 查询的 sql 语句
                String sql = "select count(*) from sale where autoPartsId = ?";
                Connection connection = DbOpenHelper.getUserConnection();
                if (connection == null) {
                    listener.error("用户信息失效，请重新登录");
                    return;
                }
                PreparedStatement ps = null;
                ResultSet rs = null;
                int res = 0;
                try {
                    ps = connection.prepareStatement(sql);
                    ps.setInt(1, autopartsId);
                    rs = ps.executeQuery();
                    // 得到查询结果
                    if (rs != null) {
                        while (rs.next()) {
                            res = rs.getInt("count(*)");
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

                listener.success(res);
            }
        }).start();
    }

}
