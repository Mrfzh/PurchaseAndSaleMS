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
import com.feng.purchaseandsalems.db.StaffOperation;
import com.feng.purchaseandsalems.entity.AutopartsData;
import com.feng.purchaseandsalems.entity.StaffData;
import com.feng.purchaseandsalems.util.SoftInputUtil;

public class StaffInsertActivity extends BaseActivity implements View.OnClickListener{

    private ImageView mBackIv;
    private Button mInsertBtn;
    private EditText mIdEt;
    private EditText mNameEt;
    private EditText mContactEt;
    private ProgressBar mProgressBar;

    @Override
    protected void doBeforeSetContentView() {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_staff_insert;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        mBackIv = findViewById(R.id.iv_staff_insert_back);
        mBackIv.setOnClickListener(this);
        mInsertBtn = findViewById(R.id.btn_staff_insert_insert);
        mInsertBtn.setOnClickListener(this);

        mIdEt = findViewById(R.id.et_staff_insert_id);
        mNameEt = findViewById(R.id.et_staff_insert_name);
        mContactEt = findViewById(R.id.et_staff_insert_contact);

        mProgressBar = findViewById(R.id.pb_staff_insert);
    }

    @Override
    protected void doAfterInit() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_staff_insert_back:
                finish();
                break;
            case R.id.btn_staff_insert_insert:
                if (!checkInsert()) {
                    break;
                }
                SoftInputUtil.hideSoftInput(StaffInsertActivity.this);
                mProgressBar.setVisibility(View.VISIBLE);
                // 插入员工信息
                final StaffData staffData = new StaffData(
                        Integer.parseInt(mIdEt.getText().toString()),
                        mNameEt.getText().toString().trim(),
                        mContactEt.getText().toString().trim());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        StaffOperation.insertStaff(staffData, new OperationListener() {
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
            showShortToast("请输入员工编号");
            return false;
        }
        if (mNameEt.getText().toString().trim().equals("")) {
            showShortToast("请输入员工名称");
            return false;
        }
        if (mContactEt.getText().toString().trim().equals("")) {
            showShortToast("请输入员工联系方式");
            return false;
        }

        return true;
    }
}
