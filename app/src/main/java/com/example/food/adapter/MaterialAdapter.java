package com.example.food.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.food.R;
import com.example.food.bean.FoodMenu;

import java.util.List;

public class MaterialAdapter extends RecyclerView.Adapter<MaterialHolder> {
    private List<FoodMenu.CookQuantityBean> list;
    private Context context;

    public MaterialAdapter(List list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MaterialHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_material, parent, false);
        return new MaterialHolder(view);
    }

    @Override
    public void onBindViewHolder(MaterialHolder holder, int position) {
        holder.bindView(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
