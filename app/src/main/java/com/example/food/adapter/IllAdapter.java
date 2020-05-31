package com.example.food.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.food.R;

import java.util.List;

public class IllAdapter extends RecyclerView.Adapter<IllnessHolder> {
    private List<String> list;
    private Context context;

    public IllAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public IllnessHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_ill, parent, false);
        return new IllnessHolder(v, this);
    }

    @Override
    public void onBindViewHolder(IllnessHolder holder, int position) {
        holder.bindView(list.get(position), position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void deleteItem(int position) {
        list.remove(position);
        notifyDataSetChanged();
    }
}
