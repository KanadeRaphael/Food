package com.example.food.adapter.identify;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.food.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PickerHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.item_picker)
    TextView picker;

    public PickerHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindView(String text) {
        picker.setText(text);
    }
}
