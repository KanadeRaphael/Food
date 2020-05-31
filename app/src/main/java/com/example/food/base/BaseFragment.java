package com.example.food.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.food.application.Food;
import com.example.food.bean.MyUser;
import com.example.food.utils.WebUtil;
import com.orhanobut.logger.Logger;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {
    private MyUser user;
    private View parentView;
    private FragmentActivity activity;
    protected boolean isPrepared;
    protected boolean isVisible;
    private Unbinder unbinder;
    private WebUtil webUtil;


    public abstract
    @LayoutRes
    int getLayoutResId();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {
        webUtil = WebUtil.getInstance();
        parentView = inflater.inflate(getLayoutResId(), container, false);
        activity = getSupportActivity();
        return parentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        initView(savedInstanceState);
        this.user = Food.user;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public WebUtil getWebUtil() {
        return webUtil;
    }

    /**
     * 初始化UI
     */
    public abstract void initView(Bundle state);


    public FragmentActivity getSupportActivity() {
        return (FragmentActivity) super.getActivity();
    }


    public android.app.ActionBar getSupportActionBar() {
        return getSupportActivity().getActionBar();
    }

    public Context getApplicationContext() {
        return this.activity == null ? (getActivity() == null ?
                null : getActivity().getApplicationContext()) : this.activity.getApplicationContext();
    }

    /**
     * Fragment数据的懒加载
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    /**
     * fragment显示时才加载数据
     */
    protected void onVisible() {
        lazyLoad();
    }

    /**
     * fragment懒加载方法
     */
    protected void lazyLoad() {
    }

    /**
     * fragment隐藏
     */
    protected void onInvisible() {
    }

    /**
     * 加载数据
     */
    protected void loadData() {
    }

    /**
     * 显示进度条
     */
    protected void showProgressBar() {
    }

    /**
     * 隐藏进度条
     */
    protected void hideProgressBar() {
    }

    /**
     * 初始化recyclerView
     */
    protected void initRecyclerView() {
    }

    /**
     * 初始化refreshLayout
     */
    protected void initRefreshLayout() {
    }

    /**
     * 设置数据显示
     */
    protected void finishTask() {
    }

    protected void upUser() {
        Food.user = user;
        Logger.d("用户信息已修改" + Food.user.toString());
    }

}
