package com.feng.purchaseandsalems.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.feng.purchaseandsalems.R;
import com.feng.purchaseandsalems.base.BaseActivity;
import com.feng.purchaseandsalems.constant.MySqlConfig;

public class MysqlConfigActivity extends BaseActivity implements View.OnClickListener{

    private ImageView mBackIv;
    private EditText mIpEt;
    private EditText mUserEt;
    private EditText mPasswordEt;
    private Button mApplyBtn;

    @Override
    protected void doBeforeSetContentView() {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mysql_config;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        mBackIv = findViewById(R.id.iv_mysql_config_back);
        mBackIv.setOnClickListener(this);

        mIpEt = findViewById(R.id.et_mysql_config_ip);
        mUserEt = findViewById(R.id.et_mysql_config_user);
        mPasswordEt = findViewById(R.id.et_mysql_config_password);

        mApplyBtn = findViewById(R.id.btn_mysql_config_apply);
        mApplyBtn.setOnClickListener(this);
    }

    @Override
    protected void doAfterInit() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_mysql_config_back:
                finish();
                break;
            case R.id.btn_mysql_config_apply:
                MySqlConfig.ip = mIpEt.getText().toString().trim();
                MySqlConfig.user = mUserEt.getText().toString().trim();
                MySqlConfig.passWord = mPasswordEt.getText().toString().trim();
                finish();
                break;
            default:
                break;
        }
    }
}
