package com.feng.purchaseandsalems.db;

import com.feng.purchaseandsalems.entity.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
                    // 为两个 ? 设置具体的值
                    ps.setString(1, userData.getUserName());
                    ps.setString(2, "%");
                    ps.setString(3, userData.getPassword());
                    // 执行语句
                    ps.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    registerListener.registerError("执行 sql 语句失败");
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
                // TODO 根据用户类型，授予不同的权限
                switch (userData.getUserType()) {
                    case PURCHASE:
                        break;
                    case SALE:
                        break;
                    default:
                        break;
                }
                registerListener.registerSuccess();
            }
        }).start();
    }
}
