package com.example.food.adapter;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.food.R;
import com.example.food.activity.common.MenuActivity;
import com.example.food.bean.FoodMenu;
import com.example.food.bean.History;
import com.example.food.bean.RecommendFood;
import com.example.food.utils.WebUtil;
import com.google.gson.Gson;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HistoryHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.item_hist_image)
    ImageView image;
    @BindView(R.id.item_hist_name)
    TextView name;
    @BindView(R.id.item_hist_describe)
    TextView describle;
    private FoodMenu foodMenu;

    public HistoryHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindView(History history) {
        Glide.with(itemView.getContext()).load(history.getMenu().getImage_url()).into(image);
        name.setText(history.getMenu().getName());
        WebUtil webUtil = WebUtil.getInstance();
        webUtil.getMenu(history.getMenu().getName(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                foodMenu = new Gson().fromJson(json, FoodMenu.class);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(itemView.getContext().getApplicationContext(), MenuActivity.class);
                        RecommendFood recommendFood = new RecommendFood(foodMenu, 1);
                        intent.putExtra("SEND_OBJECT", recommendFood);
                        itemView.getContext().getApplicationContext().startActivity(intent);
                    }
                });
            }
        });

    }
}
