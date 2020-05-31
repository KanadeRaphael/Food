package com.example.food.adapter.customization;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.food.R;
import com.example.food.bean.FoodMenu;

import java.util.ArrayList;

public class CustomizationAdapter extends RecyclerView.Adapter<CustomizationHolder> {
    private ArrayList<FoodMenu> list;
    private Context context;
    private int flag;

    public CustomizationAdapter(ArrayList<FoodMenu> list, Context context, int flag) {
        this.list = list;
        this.context = context;
        this.flag = flag;
    }

    @Override
    public CustomizationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_customization, parent, false);
        return new CustomizationHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomizationHolder holder, int position) {
        holder.bindView(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
