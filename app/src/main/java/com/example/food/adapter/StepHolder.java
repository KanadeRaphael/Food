package com.example.food.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.food.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepHolder extends RecyclerView.ViewHolder {


    @BindView(R.id.step_text_view)
    TextView stepTextView;

    public StepHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindView(String text) {
        stepTextView.setText(text);
    }
}
