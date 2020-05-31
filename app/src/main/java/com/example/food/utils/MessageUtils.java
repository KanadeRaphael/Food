package com.example.food.utils;

import android.widget.Toast;

import com.example.food.application.Food;

public class MessageUtils {

    public static void MakeToast(String message) {
        Toast.makeText(Food.getInstance(), message, Toast.LENGTH_SHORT).show();
    }

}
