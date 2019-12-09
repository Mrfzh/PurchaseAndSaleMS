package com.feng.purchaseandsalems.db;

import com.feng.purchaseandsalems.entity.PurchaseData;
import com.feng.purchaseandsalems.entity.StockData;
import com.feng.purchaseandsalems.entity.StockSecondData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/12/9
 */
public class StockOperation {

    /**
     * 查询是否存在某 id 的记录
     */
    public static boolean isExistId(int id) {
        // 查询的 sql 语句
        String sql = "select * from stock where id = ?";
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
     * 插入库存信息
     */
    public static void insertStock(final StockData stockData, final OperationListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 先检查是否存在该汽车配件
                if (!AutopartsOperation.isExistId(stockData.getAutoPartsId())) {
                    listener.error("不存在该汽车配件，请先插入该汽车配件的信息");
                    return;
                }

                // 插入数据的 sql 语句
                String sql = "insert into stock (autoPartsId, num, storehouseName, " +
                        "storehouseAddress) values (?, ?, ?, ?)";
                Connection connection = DbOpenHelper.getUserConnection();
                PreparedStatement ps = null;
                if (connection == null) {
                    listener.error("用户不存在，请重新登录");
                    return;
                }
                try {
                    ps = connection.prepareStatement(sql);
                    ps.setInt(1, stockData.getAutoPartsId());
                    ps.setInt(2, stockData.getNum());
                    ps.setString(3, stockData.getStorehouseName());
                    ps.setString(4, stockData.getStorehouseAddress());
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
     * 根据 id 删除库存
     */
    public static void deleteStock(final int deleteId, final OperationListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!isExistId(deleteId)) {
                    listener.error("不存在该编号的进货信息");
                    return;
                }

                // 删除数据的 sql 语句
                String sql = "delete from stock where id = ?";
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
     * 更新库存信息
     */
    public static void alterStock(final StockData stockData, final OperationListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 先检查是否存在该汽车配件
                if (!AutopartsOperation.isExistId(stockData.getAutoPartsId())) {
                    listener.error("不存在该汽车配件，请先插入该汽车配件的信息");
                    return;
                }

                deleteStock(stockData.getId(), new OperationListener() {
                    @Override
                    public void success() {
                        // 插入数据的 sql 语句
                        String sql = "insert into stock (id, autoPartsId, num, storehouseName, " +
                                "storehouseAddress) values (?, ?, ?, ?, ?)";
                        Connection connection = DbOpenHelper.getUserConnection();
                        PreparedStatement ps = null;
                        if (connection == null) {
                            listener.error("用户不存在，请重新登录");
                            return;
                        }
                        try {
                            ps = connection.prepareStatement(sql);
                            ps.setInt(1, stockData.getId());
                            ps.setInt(2, stockData.getAutoPartsId());
                            ps.setInt(3, stockData.getNum());
                            ps.setString(4, stockData.getStorehouseName());
                            ps.setString(5, stockData.getStorehouseAddress());
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
        void success(List<StockSecondData> dataList);
        void error(String errorMsg);
    }

    /**
     * 查询所有进货信息，进行多表连接
     */
    public static void queryAll(final QueryAllListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 查询的 sql 语句
                String sql = "select stock.*, autoparts.* from stock, autoparts " +
                        "where stock.autoPartsId = autoparts.id";
                Connection connection = DbOpenHelper.getUserConnection();
                if (connection == null) {
                    listener.error("用户信息失效，请重新登录");
                    return;
                }
                PreparedStatement ps = null;
                ResultSet rs = null;
                List<StockSecondData> dataList = new ArrayList<>();
                try {
                    ps = connection.prepareStatement(sql);
                    // 执行语句（执行查询语句用的是 executeQuery 方法）
                    rs = ps.executeQuery();
                    // 得到查询结果
                    if (rs != null) {
                        while (rs.next()) {
                            StockSecondData data = new StockSecondData();
                            data.setId(rs.getInt("stock.id"));
                            data.setNum(rs.getInt("num"));
                            data.setStorehouseName(rs.getString("storehouseName"));
                            data.setStorehouseAddress(rs.getString("storehouseAddress"));
                            data.setName(rs.getString("name"));
                            data.setUse(rs.getString("use"));
                            data.setPrice(rs.getFloat("price"));
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
