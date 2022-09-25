package com.lighteet.demo.holder;

import androidx.recyclerview.widget.RecyclerView;

import com.lighteet.adapter.simple.OneToManyHolderCreate;
import com.lighteet.annotations.OneToManyFactoryCreate;
import com.lighteet.demo.bean.Test2Bean;
@OneToManyFactoryCreate
public class TestOneToManyCreate extends OneToManyHolderCreate<Test2Bean> {
    @Override
    public Class<? extends RecyclerView.ViewHolder> getHolderClass(Test2Bean model) {
        if(model.type==0){
            return Test2Holder.class;
        }
        return Test2OtherHolder.class;
    }
}
