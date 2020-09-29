package com.example.base.model;


import android.text.TextUtils;

import androidx.annotation.CallSuper;

import com.example.base.util.AppExecutors;
import com.example.base.util.ContextUtils;
import com.example.base.util.FileUtils;
import com.example.base.util.GsonUtils;
import com.example.base.util.YWLogUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class MvvmBaseModel<Res> {
    private static final String CACHE_DATA = "network/data";
    private CompositeDisposable mCompositeDisposable;
    protected List<WeakReference<IBaseModelListener>> mWeakListenerList; //监听消息队列。
    private String mCacheKey = "";
    private Class<Res> mClazz;
    private String mCacheDir;

    public MvvmBaseModel() {
        mWeakListenerList = new ArrayList<>();
        mCacheDir = new StringBuilder().append(ContextUtils.getInstance().getContext().getFilesDir()).append(File.separator).append(CACHE_DATA).append(File.separator).toString();
    }

    public void setCache(String cacheKey, Class<Res> clazz) {
        mCacheKey = cacheKey;
        mClazz = clazz;
    }

    public void register(IBaseModelListener listener) {
        if (listener == null) {
            return;
        }
        synchronized (this) {
            mWeakListenerList.add(new WeakReference<IBaseModelListener>(listener));
        }
    }

    public void unRegister(IBaseModelListener listener) {
        if (listener == null) {
            return;
        }
        synchronized (this) {
            for (WeakReference<IBaseModelListener> weakReference : mWeakListenerList) {
                IBaseModelListener iBaseModelListener = weakReference.get();
                if (iBaseModelListener == listener) {
                    mWeakListenerList.remove(iBaseModelListener);
                    break;
                }
            }
        }
    }

    public void requestData() {
        if (TextUtils.isEmpty(mCacheKey)) {
            loadData();
        } else {
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    getData(mCacheKey);
                }
            });
        }
    }

    protected abstract void loadData();

    private void getData(String cacheKey) {
        try {
            YWLogUtil.e("tag", "请求时间－－－－time=" + System.currentTimeMillis());
            String json = FileUtils.readFile(mCacheDir + cacheKey);
            YWLogUtil.e("tag", "请求时间－－－－time----json=" + System.currentTimeMillis());
            if (TextUtils.isEmpty(json)) {
                loadData();
            } else {
                Res networkResponse = GsonUtils.fromLocalJson(json, mClazz);
                if (networkResponse == null) {
                    loadData();
                } else {
                    loadSuccess(networkResponse, false);
                }
            }
        } catch (FileNotFoundException e) {
            loadData();
        }

    }

    @CallSuper
    public void cancel() {
        if (mCompositeDisposable != null && mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
        }
    }

    public void addDisposable(Disposable d) {
        if (d == null) {
            return;
        }
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(d);
    }


    public void loadSuccess(final Res networkResponse, boolean isCache) {
        synchronized (this) {
            for (WeakReference<IBaseModelListener> weakReference : mWeakListenerList) {
                IBaseModelListener iBaseModelListener = weakReference.get();
                if (iBaseModelListener != null) {
                    iBaseModelListener.onLoadFinish(this, networkResponse);
                    if (isCache) {
                        saveData(networkResponse);
                    }
                }
            }
        }
    }

    private void saveData(final Res networkResponse) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                String json = GsonUtils.toJson(networkResponse);
                if (!TextUtils.isEmpty(json)) {
                    FileUtils.writeString(mCacheDir + mCacheKey, json);
                }
            }
        });
    }

    public void loadFail(final String errorMessage) {
        synchronized (this) {
            for (WeakReference<IBaseModelListener> weakReference : mWeakListenerList) {
                IBaseModelListener iBaseModelListener = weakReference.get();
                if (iBaseModelListener == null) {
                    return;
                }
                iBaseModelListener.onLoadFail(this, errorMessage);
            }
        }
    }

}
