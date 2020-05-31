package com.example.food.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.example.food.R;
import com.example.food.base.BaseDialog;
import com.example.food.utils.ConstantUtils;

public class InformationDialog extends BaseDialog {

    private TextView ageTextView;
    private TextView heightTextView;
    private TextView weightTextView;
    private TextView sexTextView;

    private ImageView ageImageView;
    private ImageView sexImageView;
    private ImageView heightImageView;
    private ImageView weightImageView;

    private Button okButton;

    private OptionsPickerView agePicker;
    private OptionsPickerView weightPicker;
    private OptionsPickerView heightPicker;
    private OptionsPickerView sexPicker;


    public InformationDialog(@NonNull Context context) {
        super(context);
        dialog = create();


        View view = View.inflate(context, R.layout.dialog_add_information, null);
        dialog.setView(view);

        ageImageView = view.findViewById(R.id.add_age_image_view);
        sexImageView = view.findViewById(R.id.add_sex_image_view);
        heightImageView = view.findViewById(R.id.add_height_image_view);
        weightImageView = view.findViewById(R.id.add_weight_image_view);


        ageTextView = view.findViewById(R.id.add_age_text_view);
        sexTextView = view.findViewById(R.id.add_sex_text_view);
        weightTextView = view.findViewById(R.id.add_weight_text_view);
        heightTextView = view.findViewById(R.id.add_height_text_view);

        okButton = view.findViewById(R.id.add_ok_button);

        agePicker = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                ageTextView.setText(ConstantUtils.ageList.get(options1));
            }
        }).build();
        agePicker.setPicker(ConstantUtils.ageList);

        sexPicker = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                sexTextView.setText(ConstantUtils.ageList.get(options1));
            }
        }).build();
        sexPicker.setPicker(ConstantUtils.sexList);

        heightPicker = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                heightTextView.setText(ConstantUtils.ageList.get(options1));
            }
        }).build();
        heightPicker.setPicker(ConstantUtils.heightList);

        weightPicker = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                weightTextView.setText(ConstantUtils.ageList.get(options1));
            }
        }).build();
        weightPicker.setPicker(ConstantUtils.weightList);

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


    }
}
