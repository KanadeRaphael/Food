package com.example.food.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.food.application.Food;
import com.example.food.bean.Element;
import com.example.food.bean.MyUser;
import com.example.food.utils.WebUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {
    private Unbinder unbinder;
    protected MyUser user;
    private WebUtil webUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.user = Food.user;
        setContentView(getLayoutId());
        unbinder = ButterKnife.bind(this);
        webUtil = WebUtil.getInstance();

        initViews(savedInstanceState);
        initToolBar();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    protected void onResume() {
        super.onResume();
        backChangeData();
    }

    public WebUtil getWebUtil() {
        return webUtil;
    }

    /**
     * 设置布局layout
     */
    public abstract int getLayoutId();

    /**
     * 初始化views
     */
    public abstract void initViews(Bundle savedInstanceState);

    /**
     * 初始化toolbar
     */
    public abstract void initToolBar();

    /**
     * 加载数据
     */
    public void loadData() {
    }

    /**
     * 显示进度条
     */
    public void showProgressBar() {
    }

    /**
     * 隐藏进度条
     */
    public void hideProgressBar() {
    }

    /**
     * 初始化recyclerView
     */
    public void initRecyclerView() {
    }

    /**
     * 初始化refreshLayout
     */
    public void initRefreshLayout() {
    }

    /**
     * 设置数据显示
     */
    public void finishTask() {
    }

    protected void upUser() {
        Food.user = user;
        Food.element = new Element(user);
    }

    /**
     * 填写完信息返回Activity调用
     */
    protected void backChangeData() {

    }
}
