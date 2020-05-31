package com.example.food.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.example.food.R;
import com.example.food.application.Food;
import com.example.food.base.BaseActivity;
import com.example.food.bean.Occupation;
import com.example.food.utils.CalculateUtils;
import com.example.food.utils.ConstantUtils;
import com.example.food.utils.MessageUtils;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AddInformationActivity extends BaseActivity {
    private TextView ageTextView;
    private TextView heightTextView;
    private TextView weightTextView;
    private TextView sexTextView;
    private TextView occupationTextView;

    private ImageView ageImageView;
    private ImageView sexImageView;
    private ImageView heightImageView;
    private ImageView weightImageView;
    private ImageView occupationImageView;

    private Button okButton;

    private OptionsPickerView agePicker;
    private OptionsPickerView weightPicker;
    private OptionsPickerView heightPicker;
    private OptionsPickerView sexPicker;
    private OptionsPickerView occupationPicker;

    private Context context;

    @Override
    public int getLayoutId() {
        return R.layout.dialog_add_information;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {

        context = this;
        ageImageView = findViewById(R.id.add_age_image_view);
        sexImageView = findViewById(R.id.add_sex_image_view);
        heightImageView = findViewById(R.id.add_height_image_view);
        weightImageView = findViewById(R.id.add_weight_image_view);
        occupationImageView = findViewById(R.id.occupation_image_view);


        ageTextView = findViewById(R.id.add_age_text_view);
        sexTextView = findViewById(R.id.add_sex_text_view);
        weightTextView = findViewById(R.id.add_weight_text_view);
        heightTextView = findViewById(R.id.add_height_text_view);
        occupationTextView = findViewById(R.id.occupation_text_view);

        okButton = findViewById(R.id.add_ok_button);

        occupationPicker = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                occupationTextView.setText(ConstantUtils.occupationList.get(options1));
                getOccupation(ConstantUtils.occupationList.get(options1));

            }
        }).build();
        occupationPicker.setPicker(ConstantUtils.occupationList);

        agePicker = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                ageTextView.setText(ConstantUtils.ageList.get(options1));
            }
        }).build();
        agePicker.setPicker(ConstantUtils.ageList);
        agePicker.setSelectOptions(25);


        sexPicker = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                sexTextView.setText(ConstantUtils.sexList.get(options1));
            }
        }).build();
        sexPicker.setPicker(ConstantUtils.sexList);


        heightPicker = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                heightTextView.setText(ConstantUtils.heightList.get(options1));
            }
        }).build();
        heightPicker.setPicker(ConstantUtils.heightList);
        heightPicker.setSelectOptions(119);

        weightPicker = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                weightTextView.setText(ConstantUtils.weightList.get(options1));
            }
        }).build();
        weightPicker.setPicker(ConstantUtils.weightList);
        weightPicker.setSelectOptions(59);


        occupationImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                occupationPicker.show();
            }
        });

        ageImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agePicker.show();
            }
        });
        sexImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sexPicker.show();
            }
        });
        heightImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                heightPicker.show();
            }
        });
        weightImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weightPicker.show();
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ageTextView.getText().toString().equals("年龄") || weightTextView.getText().toString().equals("体重")
                        || sexTextView.getText().toString().equals("性别") || heightTextView.getText().toString().equals("身高") ||
                        occupationTextView.getText().toString().equals("职业")) {
                    MessageUtils.MakeToast("请点击图片填写所有信息");
                } else {
                    user.setHeight(Integer.valueOf(heightTextView.getText().toString().split("c")[0]));
                    user.setWeight(Integer.valueOf(weightTextView.getText().toString().split("k")[0]));
                    user.setAge(Integer.valueOf(ageTextView.getText().toString().split("岁")[0]));
                    user.setSex(CalculateUtils.sex2int(sexTextView.getText().toString()));
                    user.setOccupation_name(occupationTextView.getText().toString());
                    float BMI = CalculateUtils.BMI(user.getHeight().floatValue(), user.getWeight().floatValue());
                    user.setBmi((int) BMI);
                    upUser();
                    MessageUtils.MakeToast("信息填写成功");
                    if (Food.physique == null) {
                        Food.element =
                                CalculateUtils.getElementsByOccupation(Food.user, Food.occupation);
                    } else {
                        Food.element =
                                CalculateUtils.getElementsByOccupationAndPhysique(Food.user,
                                        Food.occupation, Food.physique);
                    }
                    finish();
                }

            }
        });

    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void getOccupation(String text) {
        getWebUtil().getOccupation(text, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                Occupation occupation = new Gson().fromJson(json, Occupation.class);
                Food.occupation = occupation;
                Logger.d(Food.occupation);
            }
        });
    }
}
