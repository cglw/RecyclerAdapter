package com.lighteet.adapter.simple;

import androidx.recyclerview.widget.RecyclerView;

public abstract class OneToManyHolderCreate<T> {
   public abstract Class<? extends RecyclerView.ViewHolder> getHolderClass(T model);
}
