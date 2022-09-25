package com.lighteet.demo.holder;

import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.lighteet.adapter.BaseViewHolder;
import com.lighteet.annotations.AdapterCreate;
import com.lighteet.demo.R;
import com.lighteet.demo.bean.Test3Bean;
import com.lighteet.demo.params.SimpleParams;

@AdapterCreate(group ={"good"},excludeDefault = true)
public class Test3Holder extends BaseViewHolder<Test3Bean> {

    public Test3Holder(@NonNull ViewGroup parent) {
        super(parent);
        setOnClickListener(R.id.test, v -> {
            Toast.makeText(v.getContext(), ((SimpleParams) getAdapterParams()).index+"---", Toast.LENGTH_SHORT).show();
        });
    }



    @Override
    public void bindData(Test3Bean model, int position) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.item_test2;
    }
}
