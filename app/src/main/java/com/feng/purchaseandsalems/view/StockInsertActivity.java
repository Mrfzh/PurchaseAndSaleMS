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
import com.feng.purchaseandsalems.db.StockOperation;
import com.feng.purchaseandsalems.entity.PurchaseData;
import com.feng.purchaseandsalems.entity.StockData;
import com.feng.purchaseandsalems.util.SoftInputUtil;

public class StockInsertActivity extends BaseActivity implements View.OnClickListener{

    private ImageView mBackIv;
    private Button mInsertBtn;
    private EditText mAutopartsIdEt;
    private EditText mNumEt;
    private EditText mStorehouseNameEt;
    private EditText mStorehouseAddressEt;
    private ProgressBar mProgressBar;

    @Override
    protected void doBeforeSetContentView() {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_stock_insert;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        mBackIv = findViewById(R.id.iv_stock_insert_back);
        mBackIv.setOnClickListener(this);
        mInsertBtn = findViewById(R.id.btn_stock_insert_insert);
        mInsertBtn.setOnClickListener(this);

        mAutopartsIdEt = findViewById(R.id.et_stock_insert_autoParts_id);
        mNumEt = findViewById(R.id.et_stock_insert_num);
        mStorehouseNameEt = findViewById(R.id.et_stock_insert_storehouse_name);
        mStorehouseAddressEt = findViewById(R.id.et_stock_insert_storehouse_address);

        mProgressBar = findViewById(R.id.pb_stock_insert);
    }

    @Override
    protected void doAfterInit() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_stock_insert_back:
                finish();
                break;
            case R.id.btn_stock_insert_insert:
                if (!checkInsert()) {
                    break;
                }
                mProgressBar.setVisibility(View.VISIBLE);
                SoftInputUtil.hideSoftInput(StockInsertActivity.this);
                // 插入库存信息
                final StockData stockData = new StockData(
                        Integer.parseInt(mAutopartsIdEt.getText().toString()),
                        Integer.parseInt(mNumEt.getText().toString()),
                        mStorehouseNameEt.getText().toString().trim(),
                        mStorehouseAddressEt.getText().toString().trim());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        StockOperation.insertStock(stockData, new OperationListener() {
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
                                        if (errorMsg.equals("不存在该汽车配件，请先插入该汽车配件的信息")) {
                                            // 跳转到插入汽车配件信息活动
                                            jumpToNewActivity(AutopartsInsertActivity.class);
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
        if (mStorehouseNameEt.getText().toString().trim().equals("")) {
            showShortToast("请输入仓库名称");
            return false;
        }
        if (mStorehouseAddressEt.getText().toString().trim().equals("")) {
            showShortToast("请输入仓库地址");
            return false;
        }

        return true;
    }
}
