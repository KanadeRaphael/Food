package com.example.food.adapter.identify;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.food.R;
import com.example.food.bean.FoodMenu;

import java.util.ArrayList;

public class MaterialResultAdapter extends RecyclerView.Adapter<MaterialResultHolder> {
    private ArrayList<FoodMenu> foodMenus;
    private Context context;

    public MaterialResultAdapter(ArrayList<FoodMenu> foodMenus, Context context) {
        this.foodMenus = foodMenus;
        this.context = context;
    }

    @Override
    public MaterialResultHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_result_material, parent, false);
        return new MaterialResultHolder(view);
    }

    @Override
    public void onBindViewHolder(MaterialResultHolder holder, int position) {
        holder.bindView(foodMenus.get(position));
    }

    @Override
    public int getItemCount() {
        return foodMenus.size();
    }
}
