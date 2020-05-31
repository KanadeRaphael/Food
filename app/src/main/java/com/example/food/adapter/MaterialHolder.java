package com.example.food.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.food.R;
import com.example.food.bean.FoodMenu;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MaterialHolder extends RecyclerView.ViewHolder {


    @BindView(R.id.item_material_dosage)
    TextView dosage;
    @BindView(R.id.item_material_name)
    TextView name;

    public MaterialHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindView(FoodMenu.CookQuantityBean cookQuantityBean) {
        name.setText(cookQuantityBean.getMaterial());
        String weight = cookQuantityBean.getQuantity();
        dosage.setText(weight);
    }
}
