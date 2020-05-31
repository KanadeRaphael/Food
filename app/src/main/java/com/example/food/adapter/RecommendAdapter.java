package com.example.food.adapter;


import android.app.AlertDialog;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.food.R;
import com.example.food.activity.common.MenuActivity;
import com.example.food.bean.RecommendFood;

import java.util.List;

public class RecommendAdapter extends BaseMultiItemQuickAdapter<RecommendFood, BaseViewHolder> {
    private Intent intent;

    public RecommendAdapter(List<RecommendFood> data) {
        super(data);
        addItemType(RecommendFood.TYPE_BIG, R.layout.item_recommend_middle);
        addItemType(RecommendFood.TYPE_DETAIL, R.layout.item_recommend_detail);
        addItemType(RecommendFood.TYPE_MIDDLE, R.layout.item_recommend_middle);
    }

    @Override
    protected void convert(BaseViewHolder helper, final RecommendFood item) {
        ImageView imageView = helper.getView(R.id.recommend_item_imageview);

        Glide.with(mContext).load(item.getPicture()).into(imageView);
        View view = helper.getView(R.id.whole_layout);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MenuActivity.class);
                intent.putExtra("SEND_OBJECT", item);
                mContext.startActivity(intent);
            }
        });
        helper.setText(R.id.recommend_item_title, item.getTitle());
        if (item.getItemType() == RecommendFood.TYPE_DETAIL) {
            helper.setText(R.id.recommend_item_description, item.getDescription());
            LinearLayout detailClick = helper.getView(R.id.detail_click);
            detailClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext).setTitle("小知识")
                            .setMessage(item.getDescription());
                    AlertDialog dialog = builder.create();
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.setCancelable(true);
                    dialog.show();
                }
            });
        }
    }

    @Override
    public void loadMoreComplete() {
        super.loadMoreComplete();

    }

    @Override
    public void loadMoreEnd() {
        super.loadMoreEnd();
    }

    @Override
    public void loadMoreFail() {
        super.loadMoreFail();
    }
}
