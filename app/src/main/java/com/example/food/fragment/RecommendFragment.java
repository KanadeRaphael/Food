package com.example.food.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.food.R;
import com.example.food.adapter.RecommendAdapter;
import com.example.food.application.Food;
import com.example.food.base.BaseFragment;
import com.example.food.bean.FoodMenu;
import com.example.food.bean.RecommendFood;
import com.example.food.bean.Trick;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RecommendFragment extends BaseFragment {
    @BindView(R.id.fragment_re_recycler_view)
    RecyclerView recyclerView;
    Unbinder unbinder;
    @BindView(R.id.fragment_re_swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private RecommendAdapter adapter;
    private ArrayList<RecommendFood> datas = new ArrayList<>();
    private GridLayoutManager manager;
    private int[] indexs = new int[]{0, 1, 2};

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_recommend;
    }

    @Override
    public void initView(Bundle state) {
        initRecyclerView();
        loadData();
    }

    public static BaseFragment getInstance() {
        return new RecommendFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate com.example.a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    protected void initRecyclerView() {
        adapter = new RecommendAdapter(datas);
        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        adapter.isFirstOnly(false);
        recyclerView.setAdapter(adapter);

        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        addData();
                    }

                }, 1000);
            }
        }, recyclerView);
        adapter.setEnableLoadMore(true);
        adapter.setHeaderView(LayoutInflater.from(getContext()).
                inflate(R.layout.head_recommend, (ViewGroup) recyclerView.getParent(), false));
        adapter.setPreLoadNumber(1);
        recyclerView.setNestedScrollingEnabled(false);
        manager = new GridLayoutManager(getContext(), 2);
        adapter.setHeaderViewAsFlow(false);

        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) {
                    return 2;
                } else {
                    if (adapter.getItemViewType(position) == RecommendFood.TYPE_DETAIL) {
                        return 2;
                    } else {
                        return 1;
                    }
                }
            }
        });
        recyclerView.setLayoutManager(manager);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                changeInfor();
            }
        });
    }

    /**
     * 初始化数据
     * 病  、   体质  、  职业
     * 0-4     5-7       8-10
     */
    @Override
    protected void loadData() {
        super.loadData();
        getWebUtil().getRandomMenus(20, Food.user.getUsername(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                FoodMenu[] menus = new Gson().fromJson(json, FoodMenu[].class);
                int count = 0;
                for (int i = 0; i < menus.length; i++) {
                    if (count > 11) {
                        break;
                    } else {
                        int flag = indexs[count % 3];
                        RecommendFood recommendFood = new RecommendFood(menus[i], flag);
                        if (!recommendFood.getPicture().equals("0")) {
                            datas.add(recommendFood);
                            count++;
                        }
                    }
                }
                getWebUtil().getRandomTricks(5, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String json = response.body().string();
                        Trick[] tricks = new Gson().fromJson(json, Trick[].class);
                        int index = 0;
                        for (int i = 0; i < datas.size(); i++) {
                            if (datas.get(i).getItemType() == RecommendFood.TYPE_DETAIL) {
                                datas.get(i).setDescription(tricks[index].getContent());
                                datas.get(i).setTitle(tricks[index].getTitle());
                                index++;
                            }
                        }
                        recyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                                if (swipeRefreshLayout.isRefreshing()){
                                    swipeRefreshLayout.setRefreshing(false);
                                }
                            }
                        });
                    }
                });
            }
        });

    }

    private void addData() {
        getWebUtil().getRandomMenus(20, Food.user.getUsername(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final int originsize = datas.size();
                String json = response.body().string();
                FoodMenu[] menus = new Gson().fromJson(json, FoodMenu[].class);
                int count = 0;
                for (int i = 0; i < menus.length; i++) {
                    if (count > 7) {
                        break;
                    } else {
                        int flag = indexs[count % 3];
                        RecommendFood recommendFood = new RecommendFood(menus[i], flag);
                        if (!recommendFood.getPicture().equals("0")) {
                            datas.add(recommendFood);
                            count++;
                        }
                    }
                }

                //获取小知识
                getWebUtil().getRandomTricks(5, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String json = response.body().string();
                        Trick[] tricks = new Gson().fromJson(json, Trick[].class);
                        int index = 0;
                        for (int i = originsize; i < datas.size(); i++) {
                            if (datas.get(i).getItemType() == RecommendFood.TYPE_DETAIL) {
                                datas.get(i).setDescription(tricks[index].getContent());
                                datas.get(i).setTitle(tricks[index].getTitle());
                                index++;
                            }
                        }
                        recyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                adapter.loadMoreComplete();
                            }
                        });
                    }
                });
            }
        });
    }

    /**
     * 修改信息后修改推荐
     */
    public void changeInfor() {
        datas.clear();
        loadData();
    }
}
