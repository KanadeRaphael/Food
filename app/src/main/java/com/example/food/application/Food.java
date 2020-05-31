package com.example.food.application;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.example.food.bean.Element;
import com.example.food.bean.Illness;
import com.example.food.bean.MyUser;
import com.example.food.bean.Occupation;
import com.example.food.bean.Physique;
import com.example.food.utils.CalculateUtils;
import com.example.food.utils.ConstantUtils;
import com.example.food.utils.WebUtil;
import com.google.gson.Gson;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.youdao.sdk.app.YouDaoApplication;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Food extends Application {
    public static Food food;
    private int appFlag = 0;
    public static MyUser user = null;
    public static Physique physique = null;
    public static Occupation occupation = null;
    public static Element element = null;
    public static Illness illness = null;
    public static int flavourCount = 0;
    public static Element calculatedElement = null;
    public static int randomSeed = CalculateUtils.getWeek();

    @Override
    public void onCreate() {
        super.onCreate();
        food = this;
        init();
        initYouDao();
    }

    /**
     * 初始化所有基础的东西
     */
    private void init() {
        Logger.addLogAdapter(new AndroidLogAdapter());
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                appFlag++;
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                appFlag--;
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
        initOccupations();
        initUser();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                initToken();
            }
        });
        thread.start();

    }

    public static Food getInstance() {
        return food;
    }

    /**
     * app是否在前台
     *
     * @return true前台，false后台
     */
    public boolean isForeground() {
        return appFlag > 0;
    }


    /**
     * 初始化用户信息
     */
    private void initUser() {
        user = new  MyUser();
        user.setUsername("puppet");
    }

    /**
     * 初始化职业信息
     */
    private void initOccupations() {

        WebUtil webUtil = WebUtil.getInstance();
        webUtil.getAllOccupations(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Occupation[] occupations = new Gson().fromJson(response.body().string(), Occupation[].class);
                for (int i = 0; i < occupations.length; i++) {
                    ConstantUtils.occupationList.add(occupations[i].getOccupation_name());
                }
                Collections.shuffle(ConstantUtils.occupationList);
            }
        });

    }


    /**
     * 初始化百度API的token
     */
    private void initToken() {
        ConstantUtils.BD_ACCESS_TOKEN = getAccessToken();
    }


    private String getAccessToken() {
        String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
        String ak = ConstantUtils.BD_API_KEY;
        String sk = ConstantUtils.BD_SECRET_KEY;
        String getAccessTokenUrl = authHost
                + "grant_type=client_credentials"
                + "&client_id=" + ak
                + "&client_secret=" + sk;
        try {
            URL realUrl = new URL(getAccessTokenUrl);
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            Map<String, List<String>> map = connection.getHeaderFields();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = "";
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            JSONObject jsonObject = new JSONObject(result);
            return jsonObject.getString("access_token");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 初始化有道
     */
    private void initYouDao() {
        YouDaoApplication.init(this, ConstantUtils.YOUDAO_APPKEY);
    }
}
