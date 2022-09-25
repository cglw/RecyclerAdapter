package com.lighteet.adapter.simple;

import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lighteet.adapter.AdapterCallBack;
import com.lighteet.adapter.ItemBinder;
import com.lighteet.adapter.Strategy;

import java.util.HashMap;
import java.util.Map;

public class SimpleStrategy implements Strategy {
    //holder type
    private final SparseArray<Class<? extends RecyclerView.ViewHolder>> typeHolderArray = new SparseArray<>();
    //model holder映射
    private final Map<Class<?>, Class<? extends RecyclerView.ViewHolder>> oneToOneHolderMap = new HashMap<>();
    //model holder判断映射
    private final Map<Class<?>, OneToManyHolderCreate<?>> oneToManyHolderCreateMap = new HashMap<>();

    public <T> void registerOneToMany(Class<T> modelClass, OneToManyHolderCreate<T> oneToManayHolderCreate) {
        oneToManyHolderCreateMap.put(modelClass, oneToManayHolderCreate);
    }

    public void registerOneToOne(Class<?> modelClass, Class<? extends RecyclerView.ViewHolder> holderClass) {
        oneToOneHolderMap.put(modelClass, holderClass);
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        try {
            //取出holder class
            Class<? extends RecyclerView.ViewHolder> aClass = typeHolderArray.get(viewType);
            viewHolder = aClass.getConstructor(ViewGroup.class).newInstance(parent);
            return viewHolder;
        } catch (Exception e) {
            throw new NullPointerException(viewType + " holder create fail,Please Check your LayoutId or Check Your Constructor Method" + e.getMessage());
        }
    }

    @Override
    public int getItemViewType(Object itemData, int position) {
        Class<?> modelClass = itemData.getClass();
        Class<? extends RecyclerView.ViewHolder> holderClass = null;
        OneToManyHolderCreate oneToManyHolderCreate = oneToManyHolderCreateMap.get(modelClass);
        //优先一对多取
        if (oneToManyHolderCreate != null) {
            holderClass = oneToManyHolderCreate.getHolderClass(itemData);
        }
        //再一对一取
        if (holderClass == null) {
            holderClass = oneToOneHolderMap.get(modelClass);
        }
        //取到model的类 取出对应的holder
        if (holderClass == null) {
            throw new NullPointerException(modelClass + " not bind holder");
        }
        return getHolderClassType(holderClass);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, Object object, int position) {
        if (holder instanceof ItemBinder) {
            ((ItemBinder) holder).bindData(object, position);
        }
    }

    /**
     * 根据holder的class取 type
     */
    public int getHolderClassType(Class<? extends RecyclerView.ViewHolder> cls) {
        int index = typeHolderArray.indexOfValue(cls);
        if (index > -1) {
            return index;
        }
        int size = typeHolderArray.size();
        typeHolderArray.put(size, cls);
        return size;
    }

    @Override
    public boolean onFailedToRecycleView(@NonNull RecyclerView.ViewHolder holder) {
        if (holder instanceof AdapterCallBack) {
           return  ((AdapterCallBack) holder).onFailedToRecycleView(holder);
        }
        return false;
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        if (holder instanceof AdapterCallBack) {
            ((AdapterCallBack) holder).onViewAttachedToWindow(holder);
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        if (holder instanceof AdapterCallBack) {
            ((AdapterCallBack) holder).onViewAttachedToWindow(holder);
        }
    }
}
