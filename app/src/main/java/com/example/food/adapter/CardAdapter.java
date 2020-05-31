package com.example.food.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food.R;
import com.example.food.bean.DailyCard;

import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardHolder> {
    private Context context;
    private ArrayList<DailyCard> list;


    public CardAdapter(Context context, ArrayList<DailyCard> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public CardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card, parent, false);
        return new CardHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(CardHolder holder, int position) {
        holder.bindView(list.get(position).getPictureId(), list.get(position), context);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * 右划
     */
    public void swipeRight() {
    }

    /**
     * 左划
     */
    public void swipeLeft() {
    }
}
