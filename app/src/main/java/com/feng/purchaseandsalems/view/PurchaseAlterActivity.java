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
import com.feng.purchaseandsalems.entity.PurchaseData;
import com.feng.purchaseandsalems.util.SoftInputUtil;

public class PurchaseAlterActivity extends BaseActivity implements View.OnClickListener{

    private ImageView mBackIv;
    private Button mAlterBtn;
    private EditText mAutopartsIdEt;
    private EditText mNumEt;
    private EditText mFactoryNameEt;
    private EditText mFactoryAddressEt;
    private EditText mFactoryContactEt;
    private EditText mStaffIdEt;
    private ProgressBar mProgressBar;
    
    private int mAlterId;   

    @Override
    protected void doBeforeSetContentView() {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_purchase_alter;
    }

    @Override
    protected void initData() {
        mAlterId = getIntent().getIntExtra("AlterId", 0);
    }

    @Override
    protected void initView() {
        mBackIv = findViewById(R.id.iv_purchase_alter_back);
        mBackIv.setOnClickListener(this);
        mAlterBtn = findViewById(R.id.btn_purchase_alter_alter);
        mAlterBtn.setOnClickListener(this);

        mAutopartsIdEt = findViewById(R.id.et_purchase_alter_autoParts_id);
        mNumEt = findViewById(R.id.et_purchase_alter_num);
        mFactoryNameEt = findViewById(R.id.et_purchase_alter_factory_name);
        mFactoryAddressEt = findViewById(R.id.et_purchase_alter_factory_address);
        mFactoryContactEt = findViewById(R.id.et_purchase_alter_factory_contact);
        mStaffIdEt = findViewById(R.id.et_purchase_alter_staff_id);

        mProgressBar = findViewById(R.id.pb_purchase_alter);
    }

    @Override
    protected void doAfterInit() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_purchase_alter_back:
                finish();
                break;
            case R.id.btn_purchase_alter_alter:
                if (!checkInsert()) {
                    break;
                }
                mProgressBar.setVisibility(View.VISIBLE);
                SoftInputUtil.hideSoftInput(PurchaseAlterActivity.this);
                // 更新进货信息
                final PurchaseData purchaseData = new PurchaseData(mAlterId,
                        Integer.parseInt(mAutopartsIdEt.getText().toString()),
                        Integer.parseInt(mNumEt.getText().toString()),
                        mFactoryNameEt.getText().toString().trim(),
                        mFactoryAddressEt.getText().toString().trim(),
                        mFactoryContactEt.getText().toString().trim(),
                        Integer.parseInt(mStaffIdEt.getText().toString()));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        PurchaseOperation.alterPurchase(purchaseData, new OperationListener() {
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
        if (mFactoryNameEt.getText().toString().trim().equals("")) {
            showShortToast("请输入厂家名称");
            return false;
        }
        if (mFactoryAddressEt.getText().toString().trim().equals("")) {
            showShortToast("请输入厂家地址");
            return false;
        }
        if (mFactoryContactEt.getText().toString().trim().equals("")) {
            showShortToast("请输入厂家联系方式");
            return false;
        }
        if (mStaffIdEt.getText().toString().trim().equals("")) {
            showShortToast("请输入经手人编号");
            return false;
        }

        return true;
    }
}
