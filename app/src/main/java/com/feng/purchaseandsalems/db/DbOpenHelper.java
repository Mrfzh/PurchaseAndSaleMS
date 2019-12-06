package com.feng.purchaseandsalems.db;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

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
    private static String ip = "192.168.43.4";  // 安装了 mysql 的电脑的 ip 地址
    private static String dbName = "Purchase_And_Sale";    // 要连接的数据库
    private static String url = "jdbc:mysql://" + ip + ":3306/" + dbName
            + "?useUnicode=true&characterEncoding=utf8";    // mysql 数据库连接 url

    private static Connection sMainConnection;
    private static Connection sConnection;

    /**
     * 连接 root
     */
    public static Connection getMainConnection() {
        if (sMainConnection == null) {
            try {
                Class.forName(driver);  // 获取 mysql 驱动
                sMainConnection = DriverManager.getConnection(url, "root", "Aa123#");   // 获取连接
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
                    errorMsg = "该用户不存在";
                } catch (SQLException e) {
                    Log.d(TAG, "SQLException: " + e.getMessage());
                    e.printStackTrace();
                }

                final String fErrorMsg = errorMsg;

                // 切回主线程
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (sConnection != null) {
                            loginListener.loginSuccess();
                        } else {
                            loginListener.loginError(fErrorMsg);
                        }
                    }
                });
            }
        }).start();
    }

    /**
     * 连接用户数据库
     */
    public static Connection getConnection() {
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