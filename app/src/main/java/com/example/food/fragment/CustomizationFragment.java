package com.example.food.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food.R;
import com.example.food.adapter.CardAdapter;
import com.example.food.base.BaseFragment;
import com.example.food.bean.DailyCard;
import com.example.food.config.CardConfig;
import com.example.food.config.CardItemTouchCallBack;
import com.example.food.config.SwipeCardLayoutManager;
import com.example.food.utils.CalculateUtils;
import com.example.food.utils.ConstantUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CustomizationFragment extends BaseFragment {

    @BindView(R.id.fragment_card_recycler_view)
    RecyclerView cardRecyclerView;
    Unbinder unbinder;

    private CardAdapter cardAdapter;
    private ArrayList<DailyCard> mDataList = new ArrayList<>();

    private int[] picList = new int[]{
            R.drawable.bg_monday,
            R.drawable.bg_tuesday,
            R.drawable.bg_wednesday,
            R.drawable.bg_thursday,
            R.drawable.bg_friday,
            R.drawable.bg_saturday,
            R.drawable.bg_sunday
    };


    @Override
    public int getLayoutResId() {
        return R.layout.fragment_customization;
    }

    @Override
    public void initView(Bundle state) {
        loadData();
        initCardRecyclerView();
    }


    public static BaseFragment getInstance() {
        return new CustomizationFragment();
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
    protected void loadData() {
        super.loadData();
        for (int i = CalculateUtils.getWeek(); i <= 7; i++) {
            DailyCard dailyCard = new DailyCard(
                    "周" + ConstantUtils.arab2Chinese(i) + "美食谱",
                    ConstantUtils.dailyDescibes[i - 1],
                    picList[i - 1]
            );
            mDataList.add(dailyCard);
        }
        for (int i = 1; i < CalculateUtils.getWeek(); i++) {
            DailyCard dailyCard = new DailyCard(
                    "周" + ConstantUtils.arab2Chinese(i) + "美食谱",
                    ConstantUtils.dailyDescibes[i - 1],
                    picList[i - 1]
            );
            mDataList.add(dailyCard);
        }
    }

    private void initCardRecyclerView() {
        CardConfig.initConfig(getContext());
        cardRecyclerView.setLayoutManager(new SwipeCardLayoutManager());
        cardAdapter = new CardAdapter(getContext(), mDataList);
        cardRecyclerView.setAdapter(cardAdapter);

        CardItemTouchCallBack callBack = new CardItemTouchCallBack(cardRecyclerView, cardAdapter, mDataList);
        //2.创建ItemTouchHelper并把callBack传进去
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callBack);
        //3.与RecyclerView关联起来
        itemTouchHelper.attachToRecyclerView(cardRecyclerView);
    }

}
