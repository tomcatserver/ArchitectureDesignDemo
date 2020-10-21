package com.example.base.item;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BaseViewHolder extends RecyclerView.ViewHolder {

    IBaseItemView mItemView;

    public BaseViewHolder(@NonNull IBaseItemView itemView) {
        super((View) itemView);
        mItemView = itemView;
    }

    public <D extends BaseItemBean> void bind(@NonNull D itemBean) {
        mItemView.setData(itemBean);
    }

}
