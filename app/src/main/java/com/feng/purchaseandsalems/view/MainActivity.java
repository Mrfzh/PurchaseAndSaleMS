package com.feng.purchaseandsalems.view;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.feng.purchaseandsalems.R;
import com.feng.purchaseandsalems.base.BaseActivity;
import com.feng.purchaseandsalems.constant.Constant;
import com.feng.purchaseandsalems.constant.UserInfo;
import com.feng.purchaseandsalems.db.DbOpenHelper;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "MainActivity";

    private Button mMySqlConfigBtn;
    private Button mLoginBtn;
    private Button mRegisterBtn;
    private Button mSwitchUserBtn;
    private TextView mCurrUserTv;
    private Button mPurchaseBtn;
    private Button mStockBtn;
    private Button mSaleBtn;

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
        mMySqlConfigBtn = findViewById(R.id.btn_main_mysql_config);
        mMySqlConfigBtn.setOnClickListener(this);
        mLoginBtn = findViewById(R.id.btn_main_login);
        mLoginBtn.setOnClickListener(this);
        mRegisterBtn = findViewById(R.id.btn_main_register);
        mRegisterBtn.setOnClickListener(this);
        mSwitchUserBtn = findViewById(R.id.btn_main_switch_user);
        mSwitchUserBtn.setOnClickListener(this);
        mCurrUserTv = findViewById(R.id.tv_main_curr_user);

        mPurchaseBtn = findViewById(R.id.btn_main_purchase_module);
        mPurchaseBtn.setOnClickListener(this);
        mStockBtn = findViewById(R.id.btn_main_stock_module);
        mStockBtn.setOnClickListener(this);
        mSaleBtn = findViewById(R.id.btn_main_sale_module);
        mSaleBtn.setOnClickListener(this);
    }

    @Override
    protected void doAfterInit() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        // 更新当前用户
        if (UserInfo.getUser() != null) {
            mCurrUserTv.setText(UserInfo.getUser().getName());
        } else {
            mCurrUserTv.setText("无");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DbOpenHelper.closeConnection();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_main_mysql_config:
                jumpToNewActivity(MysqlConfigActivity.class);
                break;
            case R.id.btn_main_login:
                // 登录
                if (UserInfo.getUser() != null) {
                    showShortToast("您已登陆，如果要登陆其他账号，请切换用户");
                } else {
                    jumpToNewActivity(LoginActivity.class);
                }
                break;
            case R.id.btn_main_register:
                // 注册
                jumpToNewActivity(RegisterActivity.class);
                break;
            case R.id.btn_main_switch_user:
                // 切换用户
                if (UserInfo.getUser() != null) {
                    // 先退出当前用户
                    DbOpenHelper.closeUserConnection();
                    UserInfo.clearUser();
                }
                // 跳转到登录页面
                jumpToNewActivity(LoginActivity.class);
                break;
            case R.id.btn_main_purchase_module:
                // 进货模块
                if (!checkPurchase()) {
                    break;
                }
                jumpToNewActivity(PurchaseActivity.class);
                break;
            case R.id.btn_main_stock_module:
                // 库存模块
                if (!checkStock()) {
                    break;
                }
                jumpToNewActivity(StockActivity.class);
                break;
            case R.id.btn_main_sale_module:
                // 销售模块
                if (!checkSale()) {
                    break;
                }
                jumpToNewActivity(SaleActivity.class);
                break;
            default:
                break;
        }
    }

    private boolean checkPurchase() {
        if (UserInfo.getUser() == null) {
            showShortToast("请先登录");
            return false;
        }
        if (UserInfo.getUser().getType().equals(Constant.USER_TYPE_SALE)) {
            showShortToast("您没有访问该模块的权限");
            return false;
        }

        return true;
    }

    private boolean checkStock() {
        if (UserInfo.getUser() == null) {
            showShortToast("请先登录");
            return false;
        }
        if (UserInfo.getUser().getType().equals(Constant.USER_TYPE_SALE) ||
                UserInfo.getUser().getType().equals(Constant.USER_TYPE_PURCHASE)) {
            showShortToast("您没有访问该模块的权限");
            return false;
        }

        return true;
    }

    private boolean checkSale() {
        if (UserInfo.getUser() == null) {
            showShortToast("请先登录");
            return false;
        }
        if (UserInfo.getUser().getType().equals(Constant.USER_TYPE_PURCHASE)) {
            showShortToast("您没有访问该模块的权限");
            return false;
        }

        return true;
    }
}
