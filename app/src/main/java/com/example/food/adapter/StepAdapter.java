package com.example.food.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.food.R;

import java.util.ArrayList;

public class StepAdapter extends RecyclerView.Adapter<StepHolder> {
    private ArrayList<String> list;
    private Context context;

    public StepAdapter(ArrayList list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public StepHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_step, parent, false);
        return new StepHolder(view);
    }

    @Override
    public void onBindViewHolder(StepHolder holder, int position) {
        holder.bindView(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
