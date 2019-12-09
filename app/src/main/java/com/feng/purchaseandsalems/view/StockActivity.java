package com.feng.purchaseandsalems.view;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.feng.purchaseandsalems.R;
import com.feng.purchaseandsalems.adapter.PurchaseAdapter;
import com.feng.purchaseandsalems.adapter.StockAdapter;
import com.feng.purchaseandsalems.base.BaseActivity;
import com.feng.purchaseandsalems.db.OperationListener;
import com.feng.purchaseandsalems.db.PurchaseOperation;
import com.feng.purchaseandsalems.db.StockOperation;
import com.feng.purchaseandsalems.entity.PurchaseData;
import com.feng.purchaseandsalems.entity.StockSecondData;
import com.feng.purchaseandsalems.util.SoftInputUtil;

import java.util.List;

public class StockActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "StockActivity";

    private ImageView mBackIv;
    private Button mInsertBtn;
    private Button mDeleteBtn;
    private EditText mDeleteIdEt;
    private Button mAlterBtn;
    private EditText mAlterIdEt;
    private Button mQueryAllBtn;
    private RecyclerView mListRv;
    private ProgressBar mProgressBar;

    @Override
    protected void doBeforeSetContentView() {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_stock;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        mBackIv = findViewById(R.id.iv_stock_back);
        mBackIv.setOnClickListener(this);

        mInsertBtn = findViewById(R.id.btn_stock_insert);
        mInsertBtn.setOnClickListener(this);
        mDeleteBtn = findViewById(R.id.btn_stock_delete);
        mDeleteBtn.setOnClickListener(this);
        mAlterBtn = findViewById(R.id.btn_stock_alter);
        mAlterBtn.setOnClickListener(this);
        mQueryAllBtn = findViewById(R.id.btn_stock_query_all);
        mQueryAllBtn.setOnClickListener(this);

        mDeleteIdEt = findViewById(R.id.et_stock_delete_id);
        mAlterIdEt = findViewById(R.id.et_stock_alter_id);

        mListRv = findViewById(R.id.rv_stock_list);
        mListRv.setLayoutManager(new LinearLayoutManager(this));

        mProgressBar = findViewById(R.id.pb_stock);
    }

    @Override
    protected void doAfterInit() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_stock_back:
                finish();
                break;
            case R.id.btn_stock_insert:
                jumpToNewActivity(StockInsertActivity.class);
                break;
            case R.id.btn_stock_delete:
                final String deleteId = mDeleteIdEt.getText().toString();
                if (deleteId.equals("")) {
                    showShortToast("请输入要删除的库存信息编号");
                    break;
                }
                mProgressBar.setVisibility(View.VISIBLE);
                SoftInputUtil.hideSoftInput(StockActivity.this);
                // 删除库存信息
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        StockOperation.deleteStock(Integer.parseInt(deleteId), new OperationListener() {
                            @Override
                            public void success() {
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mProgressBar.setVisibility(View.GONE);
                                        showShortToast("删除成功");
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
            case R.id.btn_stock_alter:
                final String alterId = mAlterIdEt.getText().toString();
                if (alterId.equals("")) {
                    showShortToast("请输入要更改的库存信息编号");
                    break;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (!StockOperation.isExistId(Integer.parseInt(alterId))) {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    showShortToast("不存在该编号的库存信息");
                                }
                            });
                        } else {
                            // 跳转到更改库存信息界面
                            Intent intent = new Intent(StockActivity.this, StockAlterActivity.class);
                            intent.putExtra("AlterId", Integer.parseInt(alterId));
                            startActivity(intent);
                        }
                    }
                }).start();
                break;
            case R.id.btn_stock_query_all:
                // 查询所有库存信息（关联汽车配件）
                mProgressBar.setVisibility(View.VISIBLE);
                SoftInputUtil.hideSoftInput(StockActivity.this);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        StockOperation.queryAll(new StockOperation.QueryAllListener() {
                            @Override
                            public void success(final List<StockSecondData> dataList) {
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mProgressBar.setVisibility(View.GONE);
                                        StockAdapter adapter = new StockAdapter(
                                                StockActivity.this, dataList);
                                        mListRv.setAdapter(adapter);
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
}
