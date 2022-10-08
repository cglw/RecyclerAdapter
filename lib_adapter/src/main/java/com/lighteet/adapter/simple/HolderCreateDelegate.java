package com.lighteet.adapter.simple;

import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

public interface HolderCreateDelegate {
    RecyclerView.ViewHolder create(ViewGroup parent);
}
