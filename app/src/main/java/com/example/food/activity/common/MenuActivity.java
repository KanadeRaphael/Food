package com.example.food.activity.common;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.food.R;
import com.example.food.adapter.MaterialAdapter;
import com.example.food.adapter.StepAdapter;
import com.example.food.application.Food;
import com.example.food.base.BaseActivity;
import com.example.food.bean.Element;
import com.example.food.bean.RecommendFood;
import com.example.food.utils.CalculateUtils;
import com.example.food.utils.MessageUtils;
import com.example.food.utils.WebUtil;
import com.github.lzyzsd.circleprogress.ArcProgress;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;

import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MenuActivity extends BaseActivity {


    @BindView(R.id.menu_protein_circle)
    ArcProgress proteinCircle;
    @BindView(R.id.menu_fat_circle)
    ArcProgress fatCircle;
    @BindView(R.id.menu_carbohydrate_circle)
    ArcProgress carbohydrateCircle;
    @BindView(R.id.menu_material_recycler_view)
    RecyclerView materialRecyclerView;
    @BindView(R.id.menu_detail_way_recycler_view)
    RecyclerView detailWayRecyclerView;
    @BindView(R.id.menu_image)
    ImageView image;
    @BindView(R.id.menu_name)
    TextView name;
    @BindView(R.id.menu_back_button)
    ImageView backButton;
    @BindView(R.id.menu_protein_text)
    TextView proteinText;
    @BindView(R.id.menu_fat_text)
    TextView fatText;
    @BindView(R.id.menu_sugar_text)
    TextView sugarText;
    @BindView(R.id.menu_boom_button)
    BoomMenuButton boomButton;
    private RecommendFood recommendFood;


    private MaterialAdapter materialAdapter;
    private StepAdapter stepAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_menu;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        Intent intent = getIntent();
        recommendFood = (RecommendFood) intent.getSerializableExtra("SEND_OBJECT");
        Glide.with(MenuActivity.this).load(recommendFood.getPicture()).into(image);
        name.setText(recommendFood.getMenu().getName());
        HashMap map = CalculateUtils.elementsProportion(recommendFood.getMenu().getElements());
        proteinCircle.setProgress((int) map.get("protein"));
        fatCircle.setProgress((int) map.get("fat"));
        carbohydrateCircle.setProgress((int) map.get("suger"));
        proteinText.setText(Double.valueOf(recommendFood.getMenu().getElements().getProtein()).intValue() + "克");
        fatText.setText(Double.valueOf(recommendFood.getMenu().getElements().getFat()).intValue() + "克");
        sugarText.setText(Double.valueOf(recommendFood.getMenu().getElements().getCarbohydrate()).intValue() + "克");
        initList();
        initBMB();
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void initList() {
        materialAdapter = new MaterialAdapter(recommendFood.getMenu().getCook_quantity(), MenuActivity.this);
        materialRecyclerView.setAdapter(materialAdapter);
        materialRecyclerView.setLayoutManager(new LinearLayoutManager(MenuActivity.this));
        materialRecyclerView.setNestedScrollingEnabled(false);

        stepAdapter = new StepAdapter(
                CalculateUtils.getStepArray(recommendFood.getMenu().getPractice()), MenuActivity.this);
        detailWayRecyclerView.setLayoutManager(new LinearLayoutManager(MenuActivity.this));
        detailWayRecyclerView.setAdapter(stepAdapter);
        detailWayRecyclerView.setNestedScrollingEnabled(false);
    }

    /**
     * 初始化悬浮按钮
     */
    private void initBMB() {
        HamButton.Builder builder = new HamButton.Builder()
                .normalTextRes(R.string.check_add_menu)
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        MessageUtils.MakeToast("已添加到记录");
                        Food.randomSeed = CalculateUtils.getSecond();

                        Element element = new Element(recommendFood.getMenu().getElements());
                        Food.user.getEaten_elements().add(element, 0.7f);

                        String username = Food.user.getUsername();
                        WebUtil.getInstance().addEatenHistory(username, recommendFood.getMenu().getName(), new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {

                            }
                        });

                    }
                });
        boomButton.addBuilder(builder);
    }

    @OnClick(R.id.menu_back_button)
    public void onViewClicked() {
        finish();
    }
}
