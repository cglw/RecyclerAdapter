package com.lighteet.adapter;

public interface ItemBinder<T> {
    void bindData(T model, int position);
    int getLayoutId();

}
