package com.lighteet.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * adapter回调给holder
 */
public interface AdapterCallBack {

    boolean onFailedToRecycleView(@NonNull RecyclerView.ViewHolder holder);

    void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder);

    void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder);
}
