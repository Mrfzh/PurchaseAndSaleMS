package com.feng.purchaseandsalems.view;

import android.view.View;
import android.widget.Button;

import com.feng.purchaseandsalems.R;
import com.feng.purchaseandsalems.base.BaseActivity;
import com.feng.purchaseandsalems.db.DbOpenHelper;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    private Button mLoginBtn;
    private Button mRegisterBtn;

    @Override
    protected void doBeforeSetContentView() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        mLoginBtn = findViewById(R.id.btn_main_login);
        mLoginBtn.setOnClickListener(this);

        mRegisterBtn = findViewById(R.id.btn_main_register);
        mRegisterBtn.setOnClickListener(this);
    }

    @Override
    protected void doAfterInit() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DbOpenHelper.closeConnection();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_main_login:
                jumpToNewActivity(LoginActivity.class);
                break;
            case R.id.btn_main_register:
                jumpToNewActivity(RegisterActivity.class);
                break;
            default:
                break;
        }
    }
}
