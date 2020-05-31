package com.example.food.adapter.identify;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.food.R;
import com.example.food.activity.common.MenuActivity;
import com.example.food.bean.FoodMenu;
import com.example.food.bean.RecommendFood;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MaterialResultHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.item_res_mat_image)
    ImageView image;
    @BindView(R.id.item_res_mat_name)
    TextView name;
    @BindView(R.id.item_res_mat_layout)
    LinearLayout wholeLayout;

    public MaterialResultHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindView(final FoodMenu foodMenu) {
        Glide.with(itemView.getContext()).load(foodMenu.getImage_url()).into(image);
        name.setText(foodMenu.getName());
        wholeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(itemView.getContext().getApplicationContext(), MenuActivity.class);
                RecommendFood recommendFood = new RecommendFood(foodMenu, 1);
                intent.putExtra("SEND_OBJECT", recommendFood);
                itemView.getContext().getApplicationContext().startActivity(intent);
            }
        });
    }
}
