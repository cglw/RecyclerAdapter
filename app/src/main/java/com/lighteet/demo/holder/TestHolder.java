package com.lighteet.demo.holder;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.lighteet.adapter.BaseViewHolder;
import com.lighteet.annotations.AdapterCreate;
import com.lighteet.demo.R;
import com.lighteet.demo.bean.TestBean;
@AdapterCreate
public class TestHolder extends BaseViewHolder<TestBean> {

    public TestHolder(@NonNull ViewGroup parent) {
        super(parent);
    }

    @Override
    public void bindData(TestBean model, int position) {
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_test;
    }
}
