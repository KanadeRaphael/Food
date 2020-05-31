package com.example.food.base;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.food.application.Food;
import com.example.food.bean.MyUser;

public abstract class BaseDialog extends AlertDialog.Builder {

    protected AlertDialog dialog;

    public BaseDialog(@NonNull Context context) {
        super(context);
    }

    protected void upUser(MyUser user) {
        Food.user = user;
    }

    public void showDialog() {
        dialog.show();
    }

}
