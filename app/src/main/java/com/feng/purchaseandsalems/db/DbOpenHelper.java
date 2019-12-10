package com.feng.purchaseandsalems.db;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.feng.purchaseandsalems.constant.MySqlConfig;
import com.feng.purchaseandsalems.constant.UserInfo;
import com.feng.purchaseandsalems.entity.User;
import com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException;
import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Feng Zhaohao
 * Created on 2019/11/24
 */
public class DbOpenHelper {
    private static final String TAG = "DbOpenHelper";

    private static String driver = "com.mysql.jdbc.Driver";// mysql 驱动
    private static String dbName = "Purchase_And_Sale";    // 要连接的数据库

    private static Connection sMainConnection;
    private static Connection sConnection;

    /**
     * 连接 root
     */
    public static Connection getMainConnection() {
        if (sMainConnection == null) {
            try {
                Class.forName(driver);  // 获取 mysql 驱动
                String url = "jdbc:mysql://" + MySqlConfig.ip + ":3306/" + dbName
                        + "?useUnicode=true&characterEncoding=utf8";
                sMainConnection = DriverManager.getConnection(url, MySqlConfig.user, MySqlConfig.passWord);   // 获取连接
            } catch (ClassNotFoundException e) {
                Log.d(TAG, "ClassNotFoundException: " + e.getMessage());
                e.printStackTrace();
            } catch (SQLException e) {
                Log.d(TAG, "SQLException: " + e.getMessage());
                e.printStackTrace();
            }
            Log.d(TAG, "getConn: success");
        }

        return sMainConnection;
    }


    /**
     * 登录：连接用户数据库
     */
    public static void login(final String user, final String password, final LoginListener loginListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String errorMsg = "数据库连接失败";

                try {
                    Class.forName(driver);  // 获取 mysql 驱动
                    String url = "jdbc:mysql://" + MySqlConfig.ip + ":3306/" + dbName
                            + "?useUnicode=true&characterEncoding=utf8";
                    sConnection = DriverManager.getConnection(url, user, password);   // 获取连接
                } catch (ClassNotFoundException e) {
                    Log.d(TAG, "ClassNotFoundException: " + e.getMessage());
                    e.printStackTrace();
                }  catch (MySQLSyntaxErrorException e) {
                    Log.d(TAG, "MySQLSyntaxErrorException: " + e.getMessage());
                    e.printStackTrace();
                } catch (MySQLNonTransientConnectionException e) {
                    Log.d(TAG, "MySQLNonTransientConnectionException: " + e.getMessage());
                    e.printStackTrace();
                    errorMsg = "数据库连接失败，可能该用户不存在";
                } catch (SQLException e) {
                    Log.d(TAG, "SQLException: " + e.getMessage());
                    e.printStackTrace();
                }

                if (sConnection != null) {
                    Connection mainConnection = DbOpenHelper.getMainConnection();
                    if (mainConnection == null) {
                        loginListener.loginError("连接数据库失败");
                        return;
                    }
                    // 获取用户类型
                    String type = DbOperation.getUserType(mainConnection, user);
                    if (type.equals("")) {
                        loginListener.loginError("连接数据库失败");
                        return;
                    }
                    // 将用户信息写入 UserInfo
                    UserInfo.setUser(new User(user, type));
                    // 登陆成功
                    loginListener.loginSuccess();
                } else {
                    loginListener.loginError(errorMsg);
                }

            }
        }).start();
    }

    /**
     * 连接用户数据库
     */
    public static Connection getUserConnection() {
        return sConnection;
    }

    /**
     * 关闭数据库
     */
    public static void closeConnection() {
        if (sMainConnection != null) {
            try {
                sMainConnection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (sConnection != null) {
            try {
                sConnection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭用户数据库
     */
    public static void closeUserConnection() {
        if (sConnection != null) {
            try {
                sConnection.close();
                sConnection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
