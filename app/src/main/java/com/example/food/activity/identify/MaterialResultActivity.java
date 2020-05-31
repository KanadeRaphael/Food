package com.example.food.activity.identify;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food.R;
import com.example.food.adapter.identify.MaterialResultAdapter;
import com.example.food.base.BaseActivity;
import com.example.food.bean.ClassifyResult;
import com.example.food.bean.FoodMenu;
import com.example.food.utils.WebUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MaterialResultActivity extends BaseActivity {


    @BindView(R.id.res_mat_recycler_view)
    RecyclerView recyclerView;
    ArrayList<ClassifyResult> classifyResults;
    ArrayList<FoodMenu> list;
    private ArrayList<String> nameList;
    private MaterialResultAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_result_material;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        Intent intent = getIntent();
        nameList = (ArrayList<String>) intent.getSerializableExtra("LIST");
        adapter = new MaterialResultAdapter(list, MaterialResultActivity.this);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(MaterialResultActivity.this));

        WebUtil.getInstance().getMenusByMaterials(nameList, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                String json = response.body().string();
                FoodMenu[] menus = new Gson().fromJson(json, FoodMenu[].class);
                for (FoodMenu foodMenu : menus) {
                    list.add(foodMenu);
                }
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);


    }
}
