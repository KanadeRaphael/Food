package com.example.food.adapter.customization;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.food.R;
import com.example.food.activity.common.MenuActivity;
import com.example.food.application.Food;
import com.example.food.bean.Element;
import com.example.food.bean.FoodMenu;
import com.example.food.bean.RecommendFood;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomizationHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.item_custom_image)
    ImageView itemMenuImage;
    @BindView(R.id.item_custom_food_name)
    TextView foodName;
    @BindView(R.id.item_custom_food_quantity)
    TextView foodQuantity;
    @BindView(R.id.item_custom_arch)
    ImageView arch;
    @BindView(R.id.item_custom_food_energy)
    TextView foodEnergy;
    @BindView(R.id.item_custom_click)
    RelativeLayout click;

    public CustomizationHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindView(final FoodMenu foodMenu) {
        Glide.with(itemView.getContext()).load(foodMenu.getImage_url()).into(itemMenuImage);
        foodName.setText(foodMenu.getName());
        try {
            Element element = Food.element.calculateData(Food.user);
            double calorieQuantity = element.getCalorie() - Food.user.getEaten_elements().getCalorie();
            double quantity = calorieQuantity / foodMenu.getCalorie();
            Random random = new Random((int) foodMenu.getElements().getCalorie());
            if (quantity > 200) {
                quantity = 150 + random.nextInt(50);
            } else if (quantity < 50) {
                quantity = 50 + random.nextInt(50);
            }
            int energy = (int) (foodMenu.getElements().getCalorie() * quantity / 100);
            energy = checkEnergy(energy);
            foodEnergy.setText(energy + "千卡");
            foodQuantity.setText((int) quantity + "克");

            final Intent intent = new Intent(itemView.getContext().getApplicationContext(), MenuActivity.class);
            RecommendFood recommendFood = new RecommendFood(foodMenu, 1);
            intent.putExtra("SEND_OBJECT", recommendFood);
            click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemView.getContext().getApplicationContext().startActivity(intent);
                }
            });
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    private int checkEnergy(int energy) {
        if (energy < 30) {
            return checkEnergy(energy * 2);
        } else if (energy > 250) {
            return checkEnergy(energy - 50);
        } else {
            return energy;
        }
    }

}
