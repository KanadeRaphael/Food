package com.example.food.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.food.R;
import com.example.food.application.Food;
import com.example.food.base.BaseActivity;
import com.example.food.bean.Physique;
import com.example.food.utils.CalculateUtils;
import com.example.food.utils.ConstantUtils;
import com.example.food.utils.MessageUtils;
import com.github.czy1121.view.TurnCardListView;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AddPhysiqueActivity extends BaseActivity {


    @BindView(R.id.add_card_list)
    TurnCardListView cardList;
    @BindView(R.id.add_back_button)
    ImageView backButton;
    @BindView(R.id.add_result_layout)
    CardView resultLayout;
    @BindView(R.id.add_bottom_content)
    RelativeLayout bottomContent;
    @BindView(R.id.add_physique_image)
    CircularImageView physiqueImage;
    @BindView(R.id.add_name_text_view)
    TextView NameTextView;
    @BindView(R.id.add_expression_text_view)
    TextView expressionTextView;
    @BindView(R.id.add_characteristic_text_view)
    TextView characteristicTextView;
    @BindView(R.id.add_mentality_text_view)
    TextView mentalityTextView;
    @BindView(R.id.add_matters_text_view)
    TextView mattersTextView;


    private String result = "";
    private String physique = "平和质";
    private float[] counter = {0, 0, 0, 0, 0, 0, 0, 0, -1};
    private String[] physiques = {"淤血质", "阴虚质", "阳虚质", "痰湿质", "湿热质", "气郁质", "气虚质", "平和质"};

    private int[] colors = {0xffdef6f9, 0xffd6eeec, 0xffB2EBF2, 0xffB2DFDB, 0xff8ed0ca, 0xff80CBC4, 0xff4DB6AC, 0xff3c948b};
    private Button[] firstButtons = new Button[ConstantUtils.questionList.length];
    private Button[] secondButtons = new Button[ConstantUtils.questionList.length];
    private Button[] thirdButtons = new Button[ConstantUtils.questionList.length];
    private TextView[] titleViews = new TextView[ConstantUtils.questionList.length];

    private Button[][] buttonList;


    @Override
    public int getLayoutId() {
        return R.layout.activity_add_physique;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        buttonList = new Button[][]{
                firstButtons,
                secondButtons,
                thirdButtons
        };
        initAddView();
    }

    @Override
    public void initToolBar() {

    }

    private void initAddView() {
        BaseAdapter adapter = new BaseAdapter() {

            @Override
            public int getCount() {
                return colors.length;
            }

            @Override
            public Object getItem(int position) {
                return position;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(final int position, View child, ViewGroup parent) {


                child = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_physique, parent,
                        false);
                child.findViewById(R.id.item_phy_image).setBackgroundColor(colors[position]);
                titleViews[position] = child.findViewById(R.id.item_phy_title_view);
                titleViews[position].setText(ConstantUtils.questionList[position]);

                firstButtons[position] = child.findViewById(R.id.item_phy_first_button);
                firstButtons[position].setBackgroundColor(colors[position] + 30);


                secondButtons[position] = child.findViewById(R.id.item_phy_second_button);
                secondButtons[position].setBackgroundColor(colors[position] + 30);


                thirdButtons[position] = child.findViewById(R.id.item_phy_third_button);
                thirdButtons[position].setBackgroundColor(colors[position] + 30);


                if (position != ConstantUtils.questionList.length - 1) {
                    firstButtons[position].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            firstButtons[position].setBackgroundColor(colors[position] - 60);
                            secondButtons[position].setBackgroundColor(colors[position] + 30);
                            thirdButtons[position].setBackgroundColor(colors[position] + 30);
                            cardList.turnTo(position + 1);
                            result += "1";
                        }
                    });
                } else {
                    firstButtons[position].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getResult();
                            resultLayout.setVisibility(View.VISIBLE);
                            cardList.setVisibility(View.INVISIBLE);
                            MessageUtils.MakeToast("已记录体质信息");

                        }
                    });
                }
                secondButtons[position].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        firstButtons[position].setBackgroundColor(colors[position] + 30);
                        thirdButtons[position].setBackgroundColor(colors[position] + 30);
                        secondButtons[position].setBackgroundColor(colors[position] - 60);
                        cardList.turnTo(position + 1);
                        result += "2";
                    }
                });
                thirdButtons[position].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        firstButtons[position].setBackgroundColor(colors[position] + 30);
                        secondButtons[position].setBackgroundColor(colors[position] + 30);
                        thirdButtons[position].setBackgroundColor(colors[position] - 60);
                        cardList.turnTo(position + 1);
                        result += "3";
                    }
                });

                for (int i = 0; i < ConstantUtils.answerList[position].length; i++) {
                    buttonList[i][position].setText(ConstantUtils.answerList[position][i]);
                    buttonList[i][position].setVisibility(View.VISIBLE);
                }
                return child;
            }

        };

        cardList.setAdapter(adapter);
        cardList.setOnTurnListener(new TurnCardListView.OnTurnListener() {
            @Override
            public void onTurned(int position) {
                bottomContent.setBackgroundColor(colors[position] - 60);
                firstButtons[position].setEnabled(true);
                secondButtons[position].setEnabled(true);
                thirdButtons[position].setEnabled(true);
            }
        });


    }

    @OnClick(R.id.add_back_button)
    public void onViewClicked() {
        finish();
    }

    /**
     * 判断体质
     */
    private void getResult() {

        float margin = 0.6f;

        String code = result.substring(1);
        char[] codes = code.toCharArray();
        Logger.d(Arrays.toString(codes));

        switch ((int) (codes[0]) - 48) {
            case 1:
                counter[0]++;
                counter[1]++;

                counter[3] += margin;
                break;
            case 2:
                counter[2]++;
                counter[4]++;
                counter[5]++;
                counter[5] += 0.3;

                counter[3] += margin;
                break;
            case 3:
                counter[7]++;

                counter[6] += margin;
                counter[3] += margin;
                break;
            default:
                Logger.e((int) (codes[0]) + "     没执行");
        }
        switch ((int) (codes[1]) - 48) {
            case 1:
                counter[0]++;

                counter[2] += margin;
                counter[3] += margin;
                break;
            case 2:
                counter[4]++;

                counter[2] += margin;
                counter[3] += margin;
                break;
            case 3:
                counter[1]++;
                counter[5]++;
                counter[7]++;
                counter[6]++;

                counter[2] += margin;
                counter[3] += margin;

                break;
        }
        switch ((int) (codes[2]) - 48) {
            case 1:
                counter[1]++;
                counter[4]++;

                counter[0] += margin;
                counter[2] += margin;
                counter[5] += margin;
                counter[6] += margin;

                break;
            case 2:
                counter[3]++;

                counter[0] += margin;
                counter[2] += margin;
                counter[5] += margin;
                counter[6] += margin;
                break;
            case 3:
                counter[7]++;

                counter[0] += margin;
                counter[2] += margin;
                counter[5] += margin;
                counter[6] += margin;
                break;
        }
        switch ((int) (codes[3]) - 48) {
            case 1:
                counter[0]++;
                counter[2]++;
                counter[5]++;
                counter[6]++;

                counter[3] += margin;
                counter[4] += margin;
                break;
            case 2:
                counter[7]++;
                counter[1]++;

                counter[3] += margin;
                counter[4] += margin;
                break;
            case 3:
                break;
        }
        switch ((int) (codes[4]) - 48) {
            case 1:
                counter[3]++;

                counter[1] += margin;
                counter[2] += margin;
                counter[4] += margin;
                counter[5] += margin;
                counter[6] += margin;
                break;
            case 2:
                counter[5]++;

                counter[1] += margin;
                counter[2] += margin;
                counter[4] += margin;
                counter[5] += margin;
                counter[6] += margin;

                break;
            case 3:
                counter[7]++;

                counter[1] += margin;
                counter[2] += margin;
                counter[4] += margin;
                counter[5] += margin;
                counter[6] += margin;
                break;
        }
        switch ((int) (codes[5]) - 48) {
            case 1:
                counter[2]++;
                counter[2]++;

                counter[0] += margin;
                counter[3] += margin;
                counter[4] += margin;
                counter[5] += margin;
                counter[6] += margin;
                break;
            case 2:
                counter[1]++;
                counter[1]++;
                counter[1]++;

                counter[0] += margin;
                counter[3] += margin;
                counter[4] += margin;
                counter[5] += margin;
                counter[6] += margin;
                break;
            case 3:
                counter[7]++;

                counter[0] += margin;
                counter[3] += margin;
                counter[4] += margin;
                counter[5] += margin;
                counter[6] += margin;
                break;
        }

        int maxIndex = -1;
        for (int i = 0; i < counter.length; i++) {
            if (counter[i] > counter[8]) {
                maxIndex = i;
                counter[8] = counter[i];
            }
        }
        physique = physiques[maxIndex];
        if (physique.equals("淤血质")) {
            physique = "瘀血质";
        }
        Logger.d(Arrays.toString(counter) + "\n" + physique);
        Physique phy = new Physique();
        phy.setPhysical_name(physique);
        phy.setCharacteristic(ConstantUtils.physiquesCharacteristics[maxIndex]);
        phy.setExpression(ConstantUtils.physiquesExpressions[maxIndex]);
        phy.setMentality(ConstantUtils.physiquesMentalitys[maxIndex]);
        phy.setMatters(ConstantUtils.physiquesMatters[maxIndex]);
        phy.setImageUrl(ConstantUtils.physiquesImageUrls[maxIndex]);
        user.setPhysical_name(physique);
        upUser();
        loadInformation(phy);

        /**
         * 加载体质信息
         */
        getWebUtil().getPhysique(physique, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                Physique physique = new Gson().fromJson(json, Physique.class);
                Food.physique = physique;
                if (Food.occupation == null) {
                    Food.element =
                            CalculateUtils.getElementsByPhysique(Food.user, Food.physique);
                } else {
                    Food.element =
                            CalculateUtils.getElementsByOccupationAndPhysique(Food.user,
                                    Food.occupation, Food.physique);
                }
            }
        });

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    /**
     * 加载显示的信息
     *
     * @param physique 体质类
     */
    private void loadInformation(Physique physique) {
        Glide.with(AddPhysiqueActivity.this).load(physique.getImageUrl()).into(physiqueImage);
        NameTextView.setText(physique.getPhysical_name());
        expressionTextView.setText(physique.getExpression());
        characteristicTextView.setText(physique.getCharacteristic());
        mentalityTextView.setText(physique.getMentality());
        mattersTextView.setText(physique.getMatters());
    }
}
