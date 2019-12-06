package com.feng.purchaseandsalems.view;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.feng.purchaseandsalems.R;
import com.feng.purchaseandsalems.base.BaseActivity;
import com.feng.purchaseandsalems.db.DbOpenHelper;
import com.feng.purchaseandsalems.db.LoginListener;
import com.feng.purchaseandsalems.util.SoftInputUtil;

public class LoginActivity extends BaseActivity implements View.OnClickListener{

    private ImageView mBackIv;
    private EditText mUserNameEt;
    private EditText mPasswordEt;
    private Button mLoginBtn;
    private ProgressBar mLoginPb;

    @Override
    protected void doBeforeSetContentView() {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);   //隐藏标题栏
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        mBackIv = findViewById(R.id.iv_login_back);
        mBackIv.setOnClickListener(this);

        mUserNameEt = findViewById(R.id.et_login_user_name);
        mPasswordEt = findViewById(R.id.et_login_password);

        mLoginBtn = findViewById(R.id.btn_login_login);
        mLoginBtn.setOnClickListener(this);

        mLoginPb = findViewById(R.id.pb_login);
    }

    @Override
    protected void doAfterInit() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_login_back:
                finish();
                break;
            case R.id.btn_login_login:
                if (!checkLogin()) {
                    break;
                }
                // 进行登录
                SoftInputUtil.hideSoftInput(LoginActivity.this);
                mLoginPb.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        login();
                    }
                }, 500);
                break;
            default:
                break;
        }
    }

    private boolean checkLogin() {
        if (mUserNameEt.getText().toString().trim().equals("")) {
            showShortToast("请输入用户名");
            return false;
        }
        if (mPasswordEt.getText().toString().trim().equals("")) {
            showShortToast("请输入密码");
            return false;
        }

        return true;
    }

    private void login() {
        DbOpenHelper.login(mUserNameEt.getText().toString(),
                mPasswordEt.getText().toString(), new LoginListener() {
                    @Override
                    public void loginSuccess() {
                        // 登录成功
                        mLoginPb.setVisibility(View.GONE);
                        showShortToast("登录成功");
                        finish();
                    }

                    @Override
                    public void loginError(String errorMsg) {
                        // 登录失败
                        mLoginPb.setVisibility(View.GONE);
                        showShortToast(errorMsg);
                    }
                });
    }
}
