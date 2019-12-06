package com.feng.purchaseandsalems.app;

import android.app.Application;

import com.feng.purchaseandsalems.db.DbOpenHelper;

/**
 * @author Feng Zhaohao
 * Created on 2019/12/6
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        new Thread(new Runnable() {
            @Override
            public void run() {
                DbOpenHelper.getMainConnection();
            }
        }).start();
    }

}
