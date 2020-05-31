package com.example.food.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.food.R;
import com.example.food.activity.customization.CustomizationActivity;
import com.example.food.application.Food;
import com.example.food.bean.DailyCard;
import com.example.food.utils.MessageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CardHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.item_card_photo)
    ImageView photo;
    @BindView(R.id.item_card_name)
    TextView name;
    @BindView(R.id.item_card_sign)
    TextView sign;

    private View itemView;
    private Intent i;

    public CardHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.itemView = itemView;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void bindView(int picId, DailyCard dailyCard, final Context context) {
        name.setText(dailyCard.getTitle());
        sign.setText(dailyCard.getDescription());
        Glide.with(context).load(picId).into(photo);
        i = new Intent(context, CustomizationActivity.class);
        i.putExtra("SEND_CODE", dailyCard.getTitle());
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Food.physique == null || Food.occupation == null) {
                    MessageUtils.MakeToast("填写个人信息后才能使用");
                } else {
                    context.startActivity(i);
                }
            }
        });
    }


}
