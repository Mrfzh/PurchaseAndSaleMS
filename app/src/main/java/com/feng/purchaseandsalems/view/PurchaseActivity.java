package com.feng.purchaseandsalems.view;

import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.feng.purchaseandsalems.R;
import com.feng.purchaseandsalems.base.BaseActivity;

public class PurchaseActivity extends BaseActivity implements View.OnClickListener{

    private ImageView mBackIv;
    private Button mInsertBtn;
    private Button mDeleteBtn;
    private Button mAlterBtn;
    private Button mQueryBtn;

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
        mQueryBtn = findViewById(R.id.btn_purchase_query);
        mQueryBtn.setOnClickListener(this);
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
                break;
            case R.id.btn_purchase_alter:
                break;
            case R.id.btn_purchase_query:
                break;
            default:
                break;
        }
    }
}
