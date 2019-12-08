package com.feng.purchaseandsalems.db;

import android.util.Log;

import com.feng.purchaseandsalems.entity.AutopartsData;
import com.feng.purchaseandsalems.entity.PurchaseData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Feng Zhaohao
 * Created on 2019/12/8
 */
public class AutopartsOperation {

    /**
     * 查询是否存在某 id 的记录
     */
    static boolean isExistId(int id) {
        // 查询的 sql 语句
        String sql = "select * from autoparts where id = ?";
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
     * 插入汽车配件信息
     */
    public static void insertPurchase(final AutopartsData autopartsData, final OperationListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 插入数据的 sql 语句
//                String sql = "insert into autoparts (id, name, use, " +
//                        "price) values (?, ?, ?, ?)";
                String sql = "INSERT INTO `purchase_and_sale`.`autoparts`(`id`, `name`, `use`, `price`)" +
                        " VALUES (?, ?, ?, ?)";
                Connection connection = DbOpenHelper.getUserConnection();
                PreparedStatement ps = null;
                if (connection == null) {
                    listener.error("用户不存在，请重新登录");
                    return;
                }
                try {
                    ps = connection.prepareStatement(sql);
                    ps.setInt(1, autopartsData.getId());
                    ps.setString(2, autopartsData.getName());
                    ps.setString(3, autopartsData.getUse());
                    ps.setFloat(4, autopartsData.getPrice());
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
