package com.lighteet.adapter;

import android.content.Context;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public abstract class MultiTypeAdapter extends RecyclerView.Adapter {
    private static final String TAG = "MultiTypeAdapter";
    private Strategy strategy;
    List<?> items;
    Context context;
    public Object adapterParams;

    public <T>void setAdapterParams(Object adapterParams) {
        this.adapterParams = adapterParams;
    }

    public Object getAdapterParams() {
        return adapterParams;
    }

    public MultiTypeAdapter(Context context) {
        this.context = context;
        this.items = new ArrayList<>();
        this.strategy=createStrategy();
    }
    public MultiTypeAdapter setItems(List<?> items) {
        this.items = items;
        return this;
    }

    public List<?> getItems() {
        return items;
    }

    public void clear(){
        this.items.clear();
        notifyDataSetChanged();
    }
    public void addAll(List datas) {
        this.items.addAll(datas);
        notifyDataSetChanged();
    }


    public abstract Strategy createStrategy() ;

    public Strategy getStrategy() {
        return strategy;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return this.strategy.createViewHolder(parent,viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
         this.strategy.onBindViewHolder(holder,items.get(position),position);
    }


    @Override
    public int getItemViewType(int position) {
       return this.strategy.getItemViewType(items.get(position),position);
    }


    @Override
    public int getItemCount() {
        return this.items == null ? 0 : this.items.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public boolean onFailedToRecycleView(@NonNull RecyclerView.ViewHolder holder) {
        return this.strategy.onFailedToRecycleView(holder);
    }
    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        this.strategy.onViewDetachedFromWindow(holder);
    }
    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        this.strategy.onViewAttachedToWindow(holder);
    }
}
