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
import com.feng.purchaseandsalems.db.AutopartsOperation;
import com.feng.purchaseandsalems.db.OperationListener;
import com.feng.purchaseandsalems.db.PurchaseOperation;
import com.feng.purchaseandsalems.entity.AutopartsData;
import com.feng.purchaseandsalems.entity.PurchaseData;
import com.feng.purchaseandsalems.util.SoftInputUtil;

public class AutopartsInsertActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mBackIv;
    private Button mInsertBtn;
    private EditText mIdEt;
    private EditText mNameEt;
    private EditText mUseEt;
    private EditText mPriceEt;
    private ProgressBar mProgressBar;

    @Override
    protected void doBeforeSetContentView() {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_autoparts_insert;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        mBackIv = findViewById(R.id.iv_autoparts_insert_back);
        mBackIv.setOnClickListener(this);
        mInsertBtn = findViewById(R.id.btn_autoparts_insert_insert);
        mInsertBtn.setOnClickListener(this);

        mIdEt = findViewById(R.id.et_autoparts_insert_id);
        mNameEt = findViewById(R.id.et_autoparts_insert_name);
        mUseEt = findViewById(R.id.et_autoparts_insert_use);
        mPriceEt = findViewById(R.id.et_autoparts_insert_price);

        mProgressBar = findViewById(R.id.pb_autoparts_insert);
    }

    @Override
    protected void doAfterInit() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_autoparts_insert_back:
                finish();
                break;
            case R.id.btn_autoparts_insert_insert:
                if (!checkInsert()) {
                    break;
                }
                SoftInputUtil.hideSoftInput(AutopartsInsertActivity.this);
                mProgressBar.setVisibility(View.VISIBLE);
                // 插入汽车配件信息
                final AutopartsData autopartsData = new AutopartsData(
                        Integer.parseInt(mIdEt.getText().toString()),
                        mNameEt.getText().toString().trim(),
                        mUseEt.getText().toString().trim(),
                        Float.parseFloat(mPriceEt.getText().toString()));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AutopartsOperation.insertPurchase(autopartsData, new OperationListener() {
                            @Override
                            public void success() {
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mProgressBar.setVisibility(View.GONE);
                                        showShortToast("插入成功");
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
        if (mIdEt.getText().toString().trim().equals("")) {
            showShortToast("请输入汽车配件编号");
            return false;
        }
        if (mNameEt.getText().toString().trim().equals("")) {
            showShortToast("请输入汽车配件名称");
            return false;
        }
        if (mUseEt.getText().toString().trim().equals("")) {
            showShortToast("请输入汽车配件用途");
            return false;
        }
        if (mPriceEt.getText().toString().trim().equals("")) {
            showShortToast("请输入汽车配件价格");
            return false;
        }

        return true;
    }
}
