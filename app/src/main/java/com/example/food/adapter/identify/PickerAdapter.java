package com.example.food.adapter.identify;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.food.R;

import java.util.ArrayList;


public class PickerAdapter extends RecyclerView.Adapter<PickerHolder> {
    private ArrayList<String> list;
    private Context context;

    public PickerAdapter(ArrayList<String> list, Context context) {
        super();
        this.list = list;
        this.context = context;
    }

    @Override
    public PickerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_picker, parent, false);
        return new PickerHolder(view);
    }

    @Override
    public void onBindViewHolder(PickerHolder holder, int position) {
        holder.bindView(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
