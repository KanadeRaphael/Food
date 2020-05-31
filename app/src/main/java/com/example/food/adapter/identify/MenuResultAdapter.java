package com.example.food.adapter.identify;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.food.R;
import com.example.food.activity.identify.MenuResultActivity;
import com.example.food.application.Food;
import com.example.food.bean.ClassifyResult;
import com.example.food.utils.CalculateUtils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import travel.ithaka.android.horizontalpickerlib.PickerLayoutManager;

public class MenuResultAdapter extends RecyclerView.Adapter<MenuResultHolder> {
    private ArrayList<ClassifyResult> list;
    private Context context;
    private PickerLayoutManager pickerLayoutManager;

    public MenuResultAdapter(ArrayList<ClassifyResult> list, Context context) {
        super();
        this.context = context;
        Logger.d(Food.user);
        list = CalculateUtils.getDishQuantity(list, Food.user);
    }

    @Override
    public MenuResultHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_result_menu, parent, false);
        pickerLayoutManager = new PickerLayoutManager(context, PickerLayoutManager.HORIZONTAL, false);
        return new MenuResultHolder(view);
    }

    @Override
    public void onBindViewHolder(MenuResultHolder holder, int position) {
        holder.bindView(list.get(position), pickerLayoutManager, (MenuResultActivity) context, position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
