package com.lighteet.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 定义策略基类
 */
public interface Strategy extends AdapterCallBack{
    /**
     * 根据
     *
     * @param parent   RV的onCreateViewHolder回调的parent
     * @param viewType 类型id
     * @return ViewHolder
     */
    RecyclerView.ViewHolder createViewHolder(ViewGroup parent, int viewType);

    int getItemViewType(Object itemData,int position);

    /**
     * 绑定holder数据
     *
     * @param holder
     * @param itemData
     * @param position
     */
    void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, Object itemData, int position);

  }
