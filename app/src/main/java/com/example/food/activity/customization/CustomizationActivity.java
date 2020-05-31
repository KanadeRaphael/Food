package com.example.food.activity.customization;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food.R;
import com.example.food.adapter.customization.CustomizationAdapter;
import com.example.food.application.Food;
import com.example.food.base.BaseActivity;
import com.example.food.bean.Element;
import com.example.food.bean.FoodMenu;
import com.example.food.bean.FoodMenuLight;
import com.example.food.utils.CalculateUtils;
import com.example.food.utils.WebUtil;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CustomizationActivity extends BaseActivity {

//    @BindView(R.id.custom_breakfast_energy_text)
//    TextView breakfastEnergyText;
    @BindView(R.id.custom_breakfast_recycler_view)
    RecyclerView breakfastRecyclerView;
//    @BindView(R.id.custom_lunch_energy_text)
//    TextView lunchEnergyText;
    @BindView(R.id.custom_lunch_recycler_view)
    RecyclerView lunchRecyclerView;
//    @BindView(R.id.custom_dinner_energy_text)
//    TextView dinnerEnergyText;
    @BindView(R.id.custom_dinner_recycler_view)
    RecyclerView dinnerRecyclerView;
    @BindView(R.id.custom_calorie_text)
    TextView calorieText;
    @BindView(R.id.custom_fat_text)
    TextView fatText;
    @BindView(R.id.custom_sugar_text)
    TextView sugarText;
    @BindView(R.id.custom_protein_text)
    TextView proteinText;
    @BindView(R.id.custom_change_button)
    LinearLayout changeButton;
    @BindView(R.id.custom_copy_button)
    LinearLayout copyButton;
    @BindView(R.id.custom_bar_title)
    TextView barTitle;

    private ArrayList<FoodMenu> breakfastList = new ArrayList<>();
    private ArrayList<FoodMenu> lunchList = new ArrayList<>();
    private ArrayList<FoodMenu> dinnerList = new ArrayList<>();


    private CustomizationAdapter breakfastAdapter;
    private CustomizationAdapter lunchAdapter;
    private CustomizationAdapter dinnerAdapter;

    private int start;
    private String text;

    private int breakfastCalorie = 0;
    private int lunchCalorie = 0;
    private int dinnerCalorie = 0;

    private int sugar = 0;
    private int protein = 0;
    private int fat = 0;
    private int calorie = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_customization;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        text = getIntent().getStringExtra("SEND_CODE");
        Logger.d(text);
        barTitle.setText(text);

        breakfastAdapter = new CustomizationAdapter(breakfastList, this, 0);
        dinnerAdapter = new CustomizationAdapter(dinnerList, this, 1);
        lunchAdapter = new CustomizationAdapter(lunchList, this, 2);

        breakfastRecyclerView.setAdapter(breakfastAdapter);
        dinnerRecyclerView.setAdapter(dinnerAdapter);
        lunchRecyclerView.setAdapter(lunchAdapter);

        breakfastRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        dinnerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        lunchRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadData();
        refreshUI();
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void loadData() {
        super.loadData();
        breakfastList.clear();
        dinnerList.clear();
        lunchList.clear();
        final WebUtil webUtil = WebUtil.getInstance();
        webUtil.getMenusByElements(getElementLimit(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                FoodMenuLight[] foodMenus = new Gson().fromJson(json, FoodMenuLight[].class);
                Logger.d(foodMenus.length);
                if (foodMenus.length > 50) {
                    Random random = new Random(Food.randomSeed + CalculateUtils.title2Int(text));
                    start = random.nextInt(foodMenus.length - 50);
                    for (int i = start; i < start + 50; i++) {
                        webUtil.getMenu(foodMenus[i].getName(), new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String json = response.body().string();
                                FoodMenu foodMenu = new Gson().fromJson(json, FoodMenu.class);
                                if (foodMenu.getIs_breakfast() == 1) {
                                    if (breakfastList.size() < 1) {
                                        breakfastList.add(foodMenu);
                                    }
                                } else {
                                    if (foodMenu.getName().contains("汤") || foodMenu.getName().contains("糕")
                                            || foodMenu.getImage_url().equals("0") || foodMenu.getName().contains("汁")
                                            || foodMenu.getName().contains("茶")) {

                                    } else {
                                        if (lunchList.size() == 0) {
                                            lunchList.add(foodMenu);
                                        } else if (dinnerList.size() == 0) {
                                            dinnerList.add(foodMenu);
                                        } else if (lunchList.size() < 3) {
                                            lunchList.add(foodMenu);
                                        } else if (dinnerList.size() < 3) {
                                            dinnerList.add(foodMenu);
                                        }
                                    }
                                }
                                Thread thread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        lunchRecyclerView.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                breakfastAdapter.notifyDataSetChanged();
                                                dinnerAdapter.notifyDataSetChanged();
                                                lunchAdapter.notifyDataSetChanged();
                                            }
                                        });
                                    }
                                });
                                thread.start();
                            }
                        });
                    }
                } else {
                    Random random = new Random(Food.randomSeed + CalculateUtils.title2Int(text));
                    start = random.nextInt(foodMenus.length);
                    for (int i = start; i < start + 12; i++) {
                        webUtil.getMenu(foodMenus[i].getName(), new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String json = response.body().string();
                                FoodMenu foodMenu = new Gson().fromJson(json, FoodMenu.class);
                                if (foodMenu.getIs_breakfast() == 1) {
                                    if (breakfastList.size() < 1) {
                                        breakfastList.add(foodMenu);
                                    }
                                } else {
                                    if (foodMenu.getName().contains("汤") || foodMenu.getName().contains("糕")
                                            || foodMenu.getImage_url().equals("0") || foodMenu.getName().contains("汁")
                                            || foodMenu.getName().contains("茶")) {

                                    } else {
                                        if (lunchList.size() == 0) {
                                            lunchList.add(foodMenu);
                                        } else if (dinnerList.size() == 0) {
                                            dinnerList.add(foodMenu);
                                        } else if (lunchList.size() < 3) {
                                            lunchList.add(foodMenu);
                                        } else if (dinnerList.size() < 3) {
                                            dinnerList.add(foodMenu);
                                        }
                                    }
                                }
                                Thread thread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        lunchRecyclerView.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                breakfastAdapter.notifyDataSetChanged();
                                                dinnerAdapter.notifyDataSetChanged();
                                                lunchAdapter.notifyDataSetChanged();
                                            }
                                        });
                                    }
                                });
                                thread.start();
                            }
                        });
                    }
                }
            }
        });

    }

    @OnClick({R.id.custom_change_button, R.id.custom_copy_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.custom_change_button:
                Food.randomSeed = CalculateUtils.getSecond();
                loadData();
                break;
            case R.id.custom_copy_button:
                break;
        }
    }

    private Map<String, Double> getElementLimit() {
        Map<String, Double> params = new HashMap<>();
        try {
            Element calculated = Food.element.calculateData(Food.user);
            params.put("calorie", (calculated.getCalorie() - Food.user.getEaten_elements().getCalorie()) / 4);
            params.put("fat", (calculated.getFat() - Food.user.getEaten_elements().getFat()) / 4);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return params;
    }

    /**
     * 加载元素总量
     */
    public void refreshUI() {
        try {
            Element element = Food.element.calculateData(Food.user);
            sugar = (int) (element.getCarbohydrate() - Food.user.getEaten_elements().getCarbohydrate());
            sugarText.setText(sugar + "");
            fat = (int) (element.getFat() - Food.user.getEaten_elements().getFat());
            fatText.setText(fat + "");
            calorie = (int) (element.getCalorie() - Food.user.getEaten_elements().getCalorie());
            calorieText.setText(calorie + "");
            protein = (int) (element.getProtein() - Food.user.getEaten_elements().getProtein());
            proteinText.setText(protein + "");
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加到饮食记录
     */
    private void addRecord() {
        for (int i = 0; i < breakfastList.size(); i++) {

        }
    }

}
