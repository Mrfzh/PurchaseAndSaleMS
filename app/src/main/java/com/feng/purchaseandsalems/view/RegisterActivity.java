package com.feng.purchaseandsalems.view;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.feng.purchaseandsalems.R;
import com.feng.purchaseandsalems.base.BaseActivity;
import com.feng.purchaseandsalems.db.DbOperation;
import com.feng.purchaseandsalems.db.RegisterListener;
import com.feng.purchaseandsalems.entity.UserData;
import com.feng.purchaseandsalems.util.SoftInputUtil;

public class RegisterActivity extends BaseActivity implements View.OnClickListener{

    private ImageView mBackIv;
    private EditText mUserNameEt;
    private EditText mPasswordEt;
    private Spinner mUserTypeSp;
    private Button mRegisterBtn;
    private ProgressBar mRegisterPb;

    private UserData.USER_TYPE mUserType;

    @Override
    protected void doBeforeSetContentView() {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);   //隐藏标题栏
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        mBackIv = findViewById(R.id.iv_register_back);
        mBackIv.setOnClickListener(this);

        mUserNameEt = findViewById(R.id.et_register_user_name);
        mPasswordEt = findViewById(R.id.et_register_password);

        mUserTypeSp = findViewById(R.id.sp_register_user_type);
        mUserTypeSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        mUserType = UserData.USER_TYPE.PURCHASE;
                        break;
                    case 1:
                        mUserType = UserData.USER_TYPE.SALE;
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mRegisterBtn = findViewById(R.id.btn_register_register);
        mRegisterBtn.setOnClickListener(this);

        mRegisterPb = findViewById(R.id.pb_register);
    }

    @Override
    protected void doAfterInit() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_register_back:
                finish();
                break;
            case R.id.btn_register_register:
                if (!checkRegister()) {
                    break;
                }
                // 进行注册
                SoftInputUtil.hideSoftInput(RegisterActivity.this);
                mRegisterPb.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        register();
                    }
                }, 500);
                break;
            default:
                break;
        }
    }

    private boolean checkRegister() {
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

    private void register() {
        UserData userData = new UserData(mUserNameEt.getText().toString().trim(),
                mPasswordEt.getText().toString().trim(),
                mUserType);
        DbOperation.register(userData, new RegisterListener() {
            @Override
            public void registerSuccess() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        mRegisterPb.setVisibility(View.GONE);
                        showShortToast("注册成功，请进行登录");
                        jumpToNewActivity(LoginActivity.class);
                        finish();
                    }
                });
            }

            @Override
            public void registerError(final String errorMsg) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        mRegisterPb.setVisibility(View.GONE);
                        showShortToast(errorMsg);
                    }
                });
            }
        });
    }
}
