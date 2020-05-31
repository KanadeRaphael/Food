package com.example.food.fragment;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.food.R;
import com.example.food.application.Food;
import com.example.food.base.BaseFragment;
import com.example.food.bean.Element;
import com.example.food.dialog.ElementDialog;
import com.example.food.utils.ChartDrawer;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.today.step.lib.ISportStepInterface;
import com.today.step.lib.TodayStepManager;
import com.today.step.lib.TodayStepService;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.itangqi.waveloadingview.WaveLoadingView;

public class HomeFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.step_text_view)
    TextView stepTextView;
    @BindView(R.id.waveLoadingView)
    WaveLoadingView waveLoadingView;
    @BindView(R.id.weight_line_chart)
    LineChart weightLineChart;
    @BindView(R.id.step_line_chart)
    LineChart stepLineChart;
    @BindView(R.id.calorie_text)
    TextView calorieText;
    @BindView(R.id.weight_text)
    TextView weightText;
    @BindView(R.id.see_whole_elements)
    LinearLayout seeAllElements;

    private int stepCount = 0;
    private static final int REFRESH_STEP_WHAT = 0;

    //循环取当前时刻的步数中间的间隔时间
    private long TIME_INTERVAL_REFRESH = 500;

    private Handler mDelayHandler = new Handler(new TodayStepCounterCall());

    private ISportStepInterface iSportStepInterface;

    private ServiceConnection coon;


    @Override
    public int getLayoutResId() {
        return R.layout.fragment_home;
    }

    @Override
    public void initView(Bundle state) {
        initPedometer();
        initChart();
    }

    public static BaseFragment getInstance() {
        return new HomeFragment();
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

    /**
     * 计步器初始化
     */
    private void initPedometer() {
        TodayStepManager.init(getActivity().getApplication());
        //开启计步
        Intent stepCounterStart = new Intent(getActivity(), TodayStepService.class);
        getActivity().startService(stepCounterStart);
        coon = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                iSportStepInterface = ISportStepInterface.Stub.asInterface(service);
                try {
                    stepCount = iSportStepInterface.getCurrentTimeSportStep();
                    updateStepCount();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                mDelayHandler.sendEmptyMessageDelayed(REFRESH_STEP_WHAT, TIME_INTERVAL_REFRESH);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        getActivity().bindService(stepCounterStart, coon, Context.BIND_AUTO_CREATE);
    }

    /**
     * 刷新步数
     */
    private void updateStepCount() {
        try {
            stepTextView.setText(stepCount + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.see_whole_elements)
    public void onViewClicked() {
        AlertDialog dialog = new ElementDialog.Builder(getContext()).create();
        dialog.show();
    }


    /**
     * 定时器，修改UI
     */
    class TodayStepCounterCall implements Handler.Callback {

        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == REFRESH_STEP_WHAT) {//每隔500毫秒获取一次计步数据刷新UI
                if (null != iSportStepInterface) {
                    int step = 0;
                    try {
                        step = iSportStepInterface.getCurrentTimeSportStep();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    if (stepCount != step) {
                        stepCount = step;
                        updateStepCount();
                    }
                }
                mDelayHandler.sendEmptyMessageDelayed(REFRESH_STEP_WHAT, TIME_INTERVAL_REFRESH);
            }
            return false;
        }
    }


    /**
     * 初始化表格
     */
    private void initChart() {
        ArrayList<Entry> weightPointValues = new ArrayList<>();
        for (int i = 1; i < 15; i++) {
            int y = (int) (Math.random() * 20);

            weightPointValues.add(new Entry(i, y));
        }
        ChartDrawer.initSingleLineChart(weightLineChart, weightPointValues, "体重");

        ArrayList<Entry> stepPointValues = new ArrayList<>();
        for (int i = 1; i < 15; i++) {
            int y = (int) (Math.random() * 20);
            stepPointValues.add(new Entry(i, y));
        }
        ChartDrawer.initSingleLineChart(stepLineChart, stepPointValues, "步数");
    }

    private int checkY(int y) {
        if (y < 7) {
            return checkY((int) (Math.random() * 10));
        } else {
            return y;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unbindService(coon);
    }

    /**
     * 更新数据
     */
    public void refreshUI() {
        if (Food.user.getBmi() != -1) {
            weightLineChart.setVisibility(View.VISIBLE);
            stepLineChart.setVisibility(View.VISIBLE);
            weightText.post(new Runnable() {
                @Override
                public void run() {
                    weightText.setText(Food.user.getWeight() + "");
                }
            });
        }else{
            stepLineChart.setVisibility(View.INVISIBLE);
            weightLineChart.setVisibility(View.INVISIBLE);
        }
        if (Food.element != null) {
            try {
                Element elementTemp = Food.element.calculateData(Food.user);
                float temp = (float) (elementTemp.getCalorie() - Food.user.getEaten_elements().getCalorie());
                calorieText.setText((int) temp + "");
                int progress = (int) (Food.user.getEaten_elements().getCalorie() / elementTemp.getCalorie()  * 100);
                waveLoadingView.setProgressValue(progress);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        refreshUI();
    }
}
