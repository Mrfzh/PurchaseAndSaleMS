package com.feng.purchaseandsalems.view;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.feng.purchaseandsalems.R;
import com.feng.purchaseandsalems.adapter.StockAdapter;
import com.feng.purchaseandsalems.base.BaseActivity;
import com.feng.purchaseandsalems.db.OperationListener;
import com.feng.purchaseandsalems.db.SaleOperation;
import com.feng.purchaseandsalems.db.StockOperation;
import com.feng.purchaseandsalems.entity.StockSecondData;
import com.feng.purchaseandsalems.util.SoftInputUtil;

import java.util.List;

public class SaleActivity extends BaseActivity implements View.OnClickListener{

    private ImageView mBackIv;
    private Button mInsertBtn;
    private Button mDeleteBtn;
    private EditText mDeleteIdEt;
    private Button mAlterBtn;
    private EditText mAlterIdEt;
    private Button mQueryBtn;
    private EditText mQueryEt;
    private TextView mCountTv;
    private ProgressBar mProgressBar;

    @Override
    protected void doBeforeSetContentView() {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sale;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        mBackIv = findViewById(R.id.iv_sale_back);
        mBackIv.setOnClickListener(this);

        mInsertBtn = findViewById(R.id.btn_sale_insert);
        mInsertBtn.setOnClickListener(this);
        mDeleteBtn = findViewById(R.id.btn_sale_delete);
        mDeleteBtn.setOnClickListener(this);
        mAlterBtn = findViewById(R.id.btn_sale_alter);
        mAlterBtn.setOnClickListener(this);
        mQueryBtn = findViewById(R.id.btn_sale_query_count);
        mQueryBtn.setOnClickListener(this);

        mDeleteIdEt = findViewById(R.id.et_sale_delete_id);
        mAlterIdEt = findViewById(R.id.et_sale_alter_id);
        mQueryEt = findViewById(R.id.et_sale_query_count);

        mCountTv = findViewById(R.id.tv_sale_count);

        mProgressBar = findViewById(R.id.pb_sale);
    }

    @Override
    protected void doAfterInit() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_sale_back:
                finish();
                break;
            case R.id.btn_sale_insert:
                jumpToNewActivity(SaleInsertActivity.class);
                break;
            case R.id.btn_sale_delete:
                final String deleteId = mDeleteIdEt.getText().toString();
                if (deleteId.equals("")) {
                    showShortToast("请输入要删除的销售信息编号");
                    break;
                }
                mProgressBar.setVisibility(View.VISIBLE);
                SoftInputUtil.hideSoftInput(SaleActivity.this);
                // 删除库存信息
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SaleOperation.deleteSale(Integer.parseInt(deleteId), new OperationListener() {
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
            case R.id.btn_sale_alter:
                final String alterId = mAlterIdEt.getText().toString();
                if (alterId.equals("")) {
                    showShortToast("请输入要更改的销售信息编号");
                    break;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (!SaleOperation.isExistId(Integer.parseInt(alterId))) {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    showShortToast("不存在该编号的销售信息");
                                }
                            });
                        } else {
                            // 跳转到更改销售信息界面
                            Intent intent = new Intent(SaleActivity.this, SaleAlterActivity.class);
                            intent.putExtra("AlterId", Integer.parseInt(alterId));
                            startActivity(intent);
                        }
                    }
                }).start();
                break;
            case R.id.btn_sale_query_count:
                if (mQueryEt.getText().toString().trim().equals("")) {
                    showShortToast("请输入要查询的汽车配件编号");
                    break;
                }
                // 查询所有库存信息（关联汽车配件）
                mProgressBar.setVisibility(View.VISIBLE);
                SoftInputUtil.hideSoftInput(SaleActivity.this);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SaleOperation.queryCount(Integer.parseInt(mQueryEt.getText().toString()),
                                new SaleOperation.QueryCountListener() {
                            @Override
                            public void success(final int count) {
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mProgressBar.setVisibility(View.GONE);
                                        mCountTv.setText(String.valueOf(count));
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
