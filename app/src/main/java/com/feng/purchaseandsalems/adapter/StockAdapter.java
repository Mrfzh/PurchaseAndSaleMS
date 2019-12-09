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
import com.feng.purchaseandsalems.entity.StockSecondData;

import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/12/9
 */
public class StockAdapter extends RecyclerView.Adapter<StockAdapter.StockViewHolder>{

    private Context mContext;
    private List<StockSecondData> mDataList;

    public StockAdapter(Context mContext, List<StockSecondData> mDataList) {
        this.mContext = mContext;
        this.mDataList = mDataList;
    }

    @NonNull
    @Override
    public StockViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new StockViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_stock, null));
    }

    @Override
    public void onBindViewHolder(@NonNull StockViewHolder stockViewHolder, int i) {
        stockViewHolder.id.setText(String.valueOf(mDataList.get(i).getId()));
        stockViewHolder.num.setText(String.valueOf(mDataList.get(i).getNum()));
        stockViewHolder.storehouseName.setText(mDataList.get(i).getStorehouseName());
        stockViewHolder.storehouseAddress.setText(mDataList.get(i).getStorehouseAddress());
        stockViewHolder.name.setText(mDataList.get(i).getName());
        stockViewHolder.use.setText(mDataList.get(i).getUse());
        stockViewHolder.price.setText(String.valueOf(mDataList.get(i).getPrice()));
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    class StockViewHolder extends RecyclerView.ViewHolder {
        TextView id;
        TextView num;
        TextView storehouseName;
        TextView storehouseAddress;
        TextView name;
        TextView use;
        TextView price;

        public StockViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.tv_stock_id);
            num = itemView.findViewById(R.id.tv_stock_num);
            storehouseName = itemView.findViewById(R.id.tv_stock_storehouse_name);
            storehouseAddress = itemView.findViewById(R.id.tv_stock_storehouse_address);
            name = itemView.findViewById(R.id.tv_stock_name);
            use = itemView.findViewById(R.id.tv_stock_use);
            price = itemView.findViewById(R.id.tv_stock_price);
        }
    }
}
