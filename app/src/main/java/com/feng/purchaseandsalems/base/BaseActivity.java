package com.feng.purchaseandsalems.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * @author Feng Zhaohao
 * Created on 2019/12/6
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doBeforeSetContentView();
        setContentView(getLayoutId());

        initData();
        initView();
        doAfterInit();
    }

    /**
     * 在setContentView方法前的操作
     */
    protected abstract void doBeforeSetContentView();

    /**
     *  获取当前活动的布局
     *
     * @return 布局id
     */
    protected abstract int getLayoutId();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 初始化视图
     */
    protected abstract void initView();

    /**
     * 初始化数据和视图后在 OnCreate 方法中的操作
     */
    protected abstract void doAfterInit();

    /**
     * 弹出Toast
     *
     * @param content 弹出的内容
     */
    protected void showShortToast(String content) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }

    /**
     * 跳转到另一活动
     *
     * @param activity 新活动.class
     */
    protected void jumpToNewActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

}
