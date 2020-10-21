package com.example.home.adpater;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.base.item.BaseViewHolder;
import com.example.home.bean.Pager2ItemBean;
import com.example.home.views.PagerNormallItemView;
import com.example.home.views.PagerSpecialItemView;

import java.util.List;

public class Page2ItemAdapter extends RecyclerView.Adapter {
    private List<Pager2ItemBean> mList;
    private static final int VIEW_TYPE_SPECIAL = 1;
    private static final int VIEW_TYPE_NORMAL = 2;
    private Context mContext;

    public Page2ItemAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<Pager2ItemBean> list) {
        mList = list;
//        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BaseViewHolder baseViewHolder = null;
        if (viewType == VIEW_TYPE_SPECIAL) {
            baseViewHolder = new BaseViewHolder(new PagerSpecialItemView(mContext));
        } else if (viewType == VIEW_TYPE_NORMAL) {
            baseViewHolder = new BaseViewHolder(new PagerNormallItemView(mContext));
        }
        return baseViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        BaseViewHolder baseViewHolder = (BaseViewHolder) holder;
        baseViewHolder.bind(mList.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        if (mList.get(position).type.equals("1")) {
            return VIEW_TYPE_SPECIAL;
        } else if (mList.get(position).type.equals("2")) {
            return VIEW_TYPE_NORMAL;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
