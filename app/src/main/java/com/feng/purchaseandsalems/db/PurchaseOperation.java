package com.feng.purchaseandsalems.db;

import com.feng.purchaseandsalems.entity.PurchaseData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author Feng Zhaohao
 * Created on 2019/12/8
 */
public class PurchaseOperation {
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
}
