package com.feng.purchaseandsalems.view;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.feng.purchaseandsalems.R;
import com.feng.purchaseandsalems.adapter.PurchaseAdapter;
import com.feng.purchaseandsalems.base.BaseActivity;
import com.feng.purchaseandsalems.db.OperationListener;
import com.feng.purchaseandsalems.db.PurchaseOperation;
import com.feng.purchaseandsalems.entity.PurchaseData;
import com.feng.purchaseandsalems.util.SoftInputUtil;

import java.util.List;

public class PurchaseActivity extends BaseActivity implements View.OnClickListener{

    private ImageView mBackIv;
    private Button mInsertBtn;
    private Button mDeleteBtn;
    private EditText mDeleteIdEt;
    private Button mAlterBtn;
    private EditText mAlterIdEt;
    private Button mQueryAllBtn;
    private Button mQueryOneBtn;
    private EditText mQueryOneEt;
    private RecyclerView mListRv;
    private ProgressBar mProgressBar;

    @Override
    protected void doBeforeSetContentView() {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);   //隐藏标题栏
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_purchase;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        mBackIv = findViewById(R.id.iv_purchase_back);
        mBackIv.setOnClickListener(this);

        mInsertBtn = findViewById(R.id.btn_purchase_insert);
        mInsertBtn.setOnClickListener(this);
        mDeleteBtn = findViewById(R.id.btn_purchase_delete);
        mDeleteBtn.setOnClickListener(this);
        mAlterBtn = findViewById(R.id.btn_purchase_alter);
        mAlterBtn.setOnClickListener(this);
        mQueryAllBtn = findViewById(R.id.btn_purchase_query_all);
        mQueryAllBtn.setOnClickListener(this);
        mQueryOneBtn = findViewById(R.id.btn_purchase_query_one);
        mQueryOneBtn.setOnClickListener(this);

        mDeleteIdEt = findViewById(R.id.et_purchase_delete_id);
        mAlterIdEt = findViewById(R.id.et_purchase_alter_id);
        mQueryOneEt = findViewById(R.id.et_purchase_query_one);

        mListRv = findViewById(R.id.rv_purchase_list);
        mListRv.setLayoutManager(new LinearLayoutManager(this));

        mProgressBar = findViewById(R.id.pb_purchase);
    }

    @Override
    protected void doAfterInit() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_purchase_back:
                finish();
                break;
            case R.id.btn_purchase_insert:
                jumpToNewActivity(PurchaseInsertActivity.class);
                break;
            case R.id.btn_purchase_delete:
                final String deleteId = mDeleteIdEt.getText().toString();
                if (deleteId.equals("")) {
                    showShortToast("请输入要删除的进货信息编号");
                    break;
                }
                mProgressBar.setVisibility(View.VISIBLE);
                SoftInputUtil.hideSoftInput(PurchaseActivity.this);
                // 删除进货信息
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        PurchaseOperation.deletePurchase(Integer.parseInt(deleteId), new OperationListener() {
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
            case R.id.btn_purchase_alter:
                final String alterId = mAlterIdEt.getText().toString();
                if (alterId.equals("")) {
                    showShortToast("请输入要更改的进货信息编号");
                    break;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (!PurchaseOperation.isExistId(Integer.parseInt(alterId))) {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    showShortToast("不存在该编号的进货信息");
                                }
                            });
                        } else {
                            // 跳转到更改进货信息界面
                            Intent intent = new Intent(PurchaseActivity.this, PurchaseAlterActivity.class);
                            intent.putExtra("AlterId", Integer.parseInt(alterId));
                            startActivity(intent);
                        }
                    }
                }).start();
                break;
            case R.id.btn_purchase_query_all:
                // 查询所有进货信息
                mProgressBar.setVisibility(View.VISIBLE);
                SoftInputUtil.hideSoftInput(PurchaseActivity.this);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        PurchaseOperation.queryAll(new PurchaseOperation.QueryAllListener() {
                            @Override
                            public void success(final List<PurchaseData> dataList) {
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mProgressBar.setVisibility(View.GONE);
                                        PurchaseAdapter adapter = new PurchaseAdapter(
                                                PurchaseActivity.this, dataList);
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
            case R.id.btn_purchase_query_one:
                // 查询单条进货信息
                final String autopartsId = mQueryOneEt.getText().toString();
                if (autopartsId.equals("")) {
                    showShortToast("请输入要查询的汽车配件编号");
                    break;
                }
                mProgressBar.setVisibility(View.VISIBLE);
                SoftInputUtil.hideSoftInput(PurchaseActivity.this);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        PurchaseOperation.queryOne(Integer.parseInt(autopartsId),
                                new PurchaseOperation.QueryOneListener() {
                            @Override
                            public void success(final List<PurchaseData> dataList) {
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mProgressBar.setVisibility(View.GONE);
                                        PurchaseAdapter adapter = new PurchaseAdapter(
                                                PurchaseActivity.this, dataList);
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
