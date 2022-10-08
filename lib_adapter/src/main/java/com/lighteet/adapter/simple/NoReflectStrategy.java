package com.lighteet.adapter.simple;

import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.Map;

public class NoReflectStrategy extends SimpleStrategy{

    protected final Map<Class<? extends RecyclerView.ViewHolder>, HolderCreateDelegate> holderCreateMap = new HashMap<>();

    public void registerHolderCreate(Class<? extends RecyclerView.ViewHolder> cls, HolderCreateDelegate holderCreate){
        holderCreateMap.put(cls,holderCreate);
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(ViewGroup parent, int viewType) {
        Class<? extends RecyclerView.ViewHolder> aClass = typeHolderArray.get(viewType);
        HolderCreateDelegate holderCreate = holderCreateMap.get(aClass);
        if(holderCreate==null){
          throw new NullPointerException(aClass+" not register holderCreate");
        }
        return holderCreate.create(parent);
    }
}
