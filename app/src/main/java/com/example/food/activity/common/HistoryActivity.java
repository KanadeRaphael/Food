package com.example.food.activity.common;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food.R;
import com.example.food.adapter.HistoryAdapter;
import com.example.food.base.BaseActivity;
import com.example.food.bean.History;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HistoryActivity extends BaseActivity {


    @BindView(R.id.hist_recycler_view)
    RecyclerView recyclerView;
    private ArrayList<History> mList = new ArrayList<>();
    private HistoryAdapter adapter;


    @Override
    public int getLayoutId() {
        return R.layout.activity_history;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        adapter = new HistoryAdapter(this, mList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        loadData();
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
        Logger.d(user.getUsername());
        getWebUtil().getEatenHistory(user.getUsername(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                History[] histories = new Gson().fromJson(json, History[].class);
                Logger.d(Arrays.toString(histories));
                mList.clear();
                for (History temp: histories) {

                    mList.add(temp);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
