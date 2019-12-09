package com.feng.purchaseandsalems.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.feng.purchaseandsalems.R;
import com.feng.purchaseandsalems.entity.PurchaseData;

import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/12/9
 */
public class PurchaseAdapter extends RecyclerView.Adapter<PurchaseAdapter.PurchaseViewHolder>{

    private Context mContext;
    private List<PurchaseData> mDataList;

    public PurchaseAdapter(Context mContext, List<PurchaseData> mDataList) {
        this.mContext = mContext;
        this.mDataList = mDataList;
    }

    @NonNull
    @Override
    public PurchaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new PurchaseViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_purchase, null));
    }

    @Override
    public void onBindViewHolder(@NonNull PurchaseViewHolder purchaseViewHolder, int i) {
        purchaseViewHolder.id.setText(String.valueOf(mDataList.get(i).getId()));
        purchaseViewHolder.autopartsId.setText(String.valueOf(mDataList.get(i).getAutoPartsId()));
        purchaseViewHolder.num.setText(String.valueOf(mDataList.get(i).getNum()));
        purchaseViewHolder.factoryName.setText(mDataList.get(i).getFactoryName());
        purchaseViewHolder.factoryAddress.setText(mDataList.get(i).getFactoryAddress());
        purchaseViewHolder.factoryContact.setText(mDataList.get(i).getFactoryContact());
        purchaseViewHolder.staffId.setText(String.valueOf(mDataList.get(i).getStaffId()));
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    class PurchaseViewHolder extends RecyclerView.ViewHolder {
        TextView id;
        TextView autopartsId;
        TextView num;
        TextView factoryName;
        TextView factoryAddress;
        TextView factoryContact;
        TextView staffId;

        public PurchaseViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.tv_purchase_id);
            autopartsId = itemView.findViewById(R.id.tv_purchase_autoparts_id);
            num = itemView.findViewById(R.id.tv_purchase_num);
            factoryName = itemView.findViewById(R.id.tv_purchase_factory_name);
            factoryAddress = itemView.findViewById(R.id.tv_purchase_factory_address);
            factoryContact = itemView.findViewById(R.id.tv_purchase_factory_contact);
            staffId = itemView.findViewById(R.id.tv_purchase_staff_id);
        }
    }
}
