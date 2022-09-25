package com.lighteet.demo.holder;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.lighteet.adapter.BaseViewHolder;
import com.lighteet.annotations.AdapterCreate;
import com.lighteet.demo.R;
import com.lighteet.demo.bean.Test2Bean;
@AdapterCreate
public class Test2OtherHolder extends BaseViewHolder<Test2Bean> {

    public Test2OtherHolder(@NonNull ViewGroup parent) {
        super(parent);
    }

    @Override
    public void bindData(Test2Bean model, int position) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.item_test3;
    }
}
