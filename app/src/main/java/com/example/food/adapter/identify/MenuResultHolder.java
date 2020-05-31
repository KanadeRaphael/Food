package com.example.food.adapter.identify;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.bumptech.glide.Glide;
import com.example.food.R;
import com.example.food.activity.identify.MenuResultActivity;
import com.example.food.bean.ClassifyResult;
import com.example.food.utils.ConstantUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import travel.ithaka.android.horizontalpickerlib.PickerLayoutManager;

public class MenuResultHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.picker)
    RecyclerView picker;

    private PickerAdapter pickerAdapter;
    private Context context;

    public MenuResultHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        context = itemView.getContext();
    }

    public void bindView(ClassifyResult classifyResult, PickerLayoutManager pickerLayoutManager,
                         final MenuResultActivity menuResultActivity, final int position) {
        int index = (int) classifyResult.getQuantity() / 2 - 2;
        pickerLayoutManager.setChangeAlpha(true);
        pickerLayoutManager.setScaleDownBy(0.99f);
        pickerLayoutManager.setScaleDownDistance(0.8f);
        picker.setLayoutManager(pickerLayoutManager);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(picker);
        pickerAdapter = new PickerAdapter(ConstantUtils.dishPicerData, context);
        picker.setAdapter(pickerAdapter);
        picker.setNestedScrollingEnabled(false);
        picker.scrollToPosition(index);
        picker.smoothScrollBy(10, 0);

        pickerLayoutManager.setOnScrollStopListener(new PickerLayoutManager.onScrollStopListener() {
            @Override
            public void selectedView(View view) {
                String text = ((TextView) view).getText().toString();
//                MessageUtils.MakeToast(text);
                menuResultActivity.refreshData(Integer.valueOf(text), position);
            }
        });


        name.setText(classifyResult.getName());
        Glide.with(context).load(classifyResult.getImgPath()).into(image);
    }
}
