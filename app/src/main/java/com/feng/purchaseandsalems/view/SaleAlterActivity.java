package com.feng.purchaseandsalems.view;

import android.os.Handler;
import android.os.Looper;
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
import com.feng.purchaseandsalems.db.OperationListener;
import com.feng.purchaseandsalems.db.PurchaseOperation;
import com.feng.purchaseandsalems.db.SaleOperation;
import com.feng.purchaseandsalems.entity.PurchaseData;
import com.feng.purchaseandsalems.entity.SaleData;
import com.feng.purchaseandsalems.util.SoftInputUtil;

public class SaleAlterActivity extends BaseActivity implements View.OnClickListener{

    private ImageView mBackIv;
    private Button mInsertBtn;
    private EditText mAutopartsIdEt;
    private EditText mNumEt;
    private EditText mCustomerNameEt;
    private EditText mCustomerContactEt;
    private EditText mStaffIdEt;
    private ProgressBar mProgressBar;

    private int mAlterId;

    @Override
    protected void doBeforeSetContentView() {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sale_alter;
    }

    @Override
    protected void initData() {
        mAlterId = getIntent().getIntExtra("AlterId", 0);
    }

    @Override
    protected void initView() {
        mBackIv = findViewById(R.id.iv_sale_alter_back);
        mBackIv.setOnClickListener(this);
        mInsertBtn = findViewById(R.id.btn_sale_alter_alter);
        mInsertBtn.setOnClickListener(this);

        mAutopartsIdEt = findViewById(R.id.et_sale_alter_autoParts_id);
        mNumEt = findViewById(R.id.et_sale_alter_num);
        mCustomerNameEt = findViewById(R.id.et_sale_alter_customer_name);
        mCustomerContactEt = findViewById(R.id.et_sale_alter_customer_contact);
        mStaffIdEt = findViewById(R.id.et_sale_alter_staff_id);

        mProgressBar = findViewById(R.id.pb_sale_alter);
    }

    @Override
    protected void doAfterInit() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_sale_alter_back:
                finish();
                break;
            case R.id.btn_sale_alter_alter:
                if (!checkInsert()) {
                    break;
                }
                mProgressBar.setVisibility(View.VISIBLE);
                SoftInputUtil.hideSoftInput(SaleAlterActivity.this);
                // 更新销售信息
                final SaleData saleData = new SaleData(mAlterId,
                        Integer.parseInt(mAutopartsIdEt.getText().toString()),
                        Integer.parseInt(mNumEt.getText().toString()),
                        mCustomerNameEt.getText().toString().trim(),
                        mCustomerContactEt.getText().toString().trim(),
                        Integer.parseInt(mStaffIdEt.getText().toString()));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SaleOperation.alterSale(saleData, new OperationListener() {
                            @Override
                            public void success() {
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mProgressBar.setVisibility(View.GONE);
                                        showShortToast("更改成功");
                                        finish();
                                    }
                                });
                            }

                            @Override
                            public void error(final String errorMsg) {
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mProgressBar.setVisibility(View.GONE);
                                        showShortToast(errorMsg);
                                        if (errorMsg.equals("不存在该汽车配件，请先插入该汽车配件的信息")) {
                                            // 跳转到插入汽车配件信息活动
                                            jumpToNewActivity(AutopartsInsertActivity.class);
                                        } else if (errorMsg.equals("不存在该员工，请先插入该汽车配件的信息")) {
                                            // 跳转到插入员工信息活动
                                            jumpToNewActivity(StaffInsertActivity.class);
                                        }
                                    }
                                });
                            }
                        });
                    }
                }, 500);
                break;
            default:
                break;
        }
    }

    private boolean checkInsert() {
        if (mAutopartsIdEt.getText().toString().trim().equals("")) {
            showShortToast("请输入汽车配件编号");
            return false;
        }
        if (mNumEt.getText().toString().trim().equals("")) {
            showShortToast("请输入汽车配件数量");
            return false;
        }
        if (mCustomerNameEt.getText().toString().trim().equals("")) {
            showShortToast("请输入客户名称");
            return false;
        }
        if (mCustomerContactEt.getText().toString().trim().equals("")) {
            showShortToast("请输入客户联系方式");
            return false;
        }
        if (mStaffIdEt.getText().toString().trim().equals("")) {
            showShortToast("请输入经手人编号");
            return false;
        }

        return true;
    }
}
