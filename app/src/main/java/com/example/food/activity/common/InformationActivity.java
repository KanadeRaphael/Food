package com.example.food.activity.common;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.food.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InformationActivity extends AppCompatActivity {

    @BindView(R.id.info_back_button)
    ImageView backButton;
    @BindView(R.id.info_title_text)
    TextView titleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.info_back_button)
    public void onViewClicked() {
        finish();
    }
}
