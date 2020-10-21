package com.example.base.model;

public interface IBaseModelListener<T> {
    void onLoadFinish(MvvmBaseModel model, T data);

    void onLoadFail(MvvmBaseModel model, String prompt);
}