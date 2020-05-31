package com.example.food.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.food.R;
import com.example.food.application.Food;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ElementDialog extends AlertDialog {
    protected ElementDialog(@NonNull Context context) {
        super(context);
    }

    protected ElementDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected ElementDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder {
        @BindView(R.id.calorie_tag)
        TextView calorieTag;
        @BindView(R.id.calorie_text)
        TextView calorieText;
        @BindView(R.id.sugar_tag)
        TextView sugarTag;
        @BindView(R.id.sugar_text)
        TextView sugarText;
        @BindView(R.id.fat_tag)
        TextView fatTag;
        @BindView(R.id.fat_text)
        TextView fatText;
        @BindView(R.id.protein_tag)
        TextView proteinTag;
        @BindView(R.id.protein_text)
        TextView proteinText;
        @BindView(R.id.cellulose_tag)
        TextView celluloseTag;
        @BindView(R.id.cellulose_text)
        TextView celluloseText;
        @BindView(R.id.vitamin_a_tag)
        TextView vitaminATag;
        @BindView(R.id.vitamin_a_text)
        TextView vitaminAText;
        @BindView(R.id.vitamin_b_1_tag)
        TextView vitaminB1Tag;
        @BindView(R.id.vitamin_b_1_text)
        TextView vitaminB1Text;
        @BindView(R.id.vitamin_b_2_tag)
        TextView vitaminB2Tag;
        @BindView(R.id.vitamin_b_2_text)
        TextView vitaminB2Text;
        @BindView(R.id.vitamin_b_6_tag)
        TextView vitaminB6Tag;
        @BindView(R.id.vitamin_b_6_text)
        TextView vitaminB6Text;
        @BindView(R.id.vitamin_c_tag)
        TextView vitaminCTag;
        @BindView(R.id.vitamin_c_text)
        TextView vitaminCText;
        @BindView(R.id.vitamin_e_tag)
        TextView vitaminETag;
        @BindView(R.id.vitamin_e_text)
        TextView vitaminEText;
        @BindView(R.id.carotene_tag)
        TextView caroteneTag;
        @BindView(R.id.carotene_text)
        TextView caroteneText;
        @BindView(R.id.cholesterol_tag)
        TextView cholesterolTag;
        @BindView(R.id.cholesterol_text)
        TextView cholesterolText;
        @BindView(R.id.ca_tag)
        TextView caTag;
        @BindView(R.id.ca_text)
        TextView caText;
        @BindView(R.id.na_tag)
        TextView naTag;
        @BindView(R.id.na_text)
        TextView naText;

        private AlertDialog dialog;

        public Builder(Context context) {

            View view = LayoutInflater.from(context).inflate(R.layout.dialog_element, null);
            dialog = new AlertDialog.Builder(context).setView(view).create();
            dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            ButterKnife.bind(this, view);
            calorieText.setText((int) (Food.user.getEaten_elements().getCalorie()) + "");
            sugarText.setText((int) (Food.user.getEaten_elements().getCarbohydrate()) + "");
            fatText.setText((int) (Food.user.getEaten_elements().getFat()) + "");
            proteinText.setText((int) Food.user.getEaten_elements().getProtein() + "");
            celluloseText.setText((int) Food.user.getEaten_elements().getCellulose() + "");
            vitaminAText.setText((int) Food.user.getEaten_elements().getVitaminA() + "");
            vitaminB1Text.setText((int) Food.user.getEaten_elements().getVitaminB1() + "");
            vitaminB2Text.setText((int) Food.user.getEaten_elements().getVitaminB2() + "");
            vitaminB6Text.setText((int) Food.user.getEaten_elements().getVitaminB6() + "");
            vitaminCText.setText((int) Food.user.getEaten_elements().getVitaminC() + "");
            vitaminEText.setText((int) Food.user.getEaten_elements().getVitaminE() + "");
            caroteneText.setText((int) Food.user.getEaten_elements().getCarotene() + "");
            cholesterolText.setText((int) Food.user.getEaten_elements().getCholesterol() + "");
            caText.setText((int) Food.user.getEaten_elements().getCa() + "");
            naText.setText((int) Food.user.getEaten_elements().getNa() + "");

        }

        public AlertDialog create() {
            return dialog;
        }
    }
}
