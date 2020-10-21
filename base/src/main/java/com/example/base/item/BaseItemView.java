package com.example.base.item;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

public abstract class BaseItemView<V extends ViewDataBinding, S extends BaseItemBean> extends LinearLayout implements IBaseItemView<S>, View.OnClickListener {
    private V mViewDataBinding;
    private S mBaseViewModel;

    public BaseItemView(Context context) {
        super(context);
        init();
    }

    public BaseItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    public BaseItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (setViewLayoutId() != 0) {
            mViewDataBinding = DataBindingUtil.inflate(inflater, setViewLayoutId(), this, false);
            mViewDataBinding.getRoot().setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    onRootClick(view);
                }
            });
            addView(mViewDataBinding.getRoot());
        }
    }

    protected abstract void onRootClick(View view);


    @Override
    public void setData(S data) {
        mBaseViewModel = data;
        setDataToView(data);
        if (mViewDataBinding != null) {
            mViewDataBinding.executePendingBindings();
        }
        onDataUpdated();

    }

    protected V getDataBinding() {
        return mViewDataBinding;
    }

    protected S getViewModel() {
        return mBaseViewModel;
    }

    protected abstract void onDataUpdated();

    protected abstract void setDataToView(S data);

    @Override
    public void onClick(View view) {

    }

    protected abstract int setViewLayoutId();

}
