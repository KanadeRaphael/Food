package com.example.food.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.food.MainActivity;
import com.example.food.application.Food;
import com.orhanobut.logger.Logger;


public class StepStarter extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        Food food = (Food) context.getApplicationContext();
        if (!food.isForeground()) {
            Intent mainIntent = new Intent(context, MainActivity.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mainIntent);
        }else {
            Logger.d("已经在计步了");
        }
    }
}
