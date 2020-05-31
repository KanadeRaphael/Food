package com.example.food.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.food.R;
import com.example.food.bean.History;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryHolder> {
    private Context context;
    private List<History> list;

    public HistoryAdapter(Context context, List list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public HistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history, parent, false);
        HistoryHolder historyHolder = new HistoryHolder(view);
        return historyHolder;
    }

    @Override
    public void onBindViewHolder(HistoryHolder holder, int position) {
        holder.bindView(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
