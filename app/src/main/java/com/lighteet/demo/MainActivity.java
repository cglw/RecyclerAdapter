package com.lighteet.demo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lighteet.adapter.MultiTypeAdapter;
import com.lighteet.annotations.ModuleAppAdapterHelper;
import com.lighteet.demo.bean.Test2Bean;
import com.lighteet.demo.bean.TestBean;
import com.lighteet.demo.params.SimpleParams;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List datas=new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MultiTypeAdapter defaultAdapter = ModuleAppAdapterHelper.getDefaultAdapter(this);
        recyclerView.setAdapter(defaultAdapter);
        defaultAdapter.setAdapterParams(new SimpleParams(100));
        for (int i = 0; i < 10; i++) {
            datas.add(new TestBean());
        }
        for (int i = 0; i < 10; i++) {
            datas.add(new Test2Bean());
        }
        for (int i = 0; i < 10; i++) {
            Test2Bean e = new Test2Bean();
            e.type = 1;
            datas.add(e);
        }
        defaultAdapter.addAll(datas);
    }

}