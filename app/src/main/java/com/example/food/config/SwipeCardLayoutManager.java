package com.example.food.config;

import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

public class SwipeCardLayoutManager extends RecyclerView.LayoutManager {
    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        //在布局之前，将所有的子View先Detach掉，放入到Scrap缓存中
        detachAndScrapAttachedViews(recycler);

        //拿到总item数量
        int itemCount = getItemCount();
        if (itemCount < 1) {
            return;
        }
        int bottomPosition;

        //确认最底层的position
        if (itemCount < CardConfig.MAX_SHOW_COUNT) {
            bottomPosition = itemCount - 1;
        } else {
            bottomPosition = CardConfig.MAX_SHOW_COUNT - 1;
        }

        for (int position = bottomPosition; position >= 0; position--) {
            //根据position找recycler要itemview
            View view = recycler.getViewForPosition(position);
            //将子View添加至RecyclerView中
            addView(view);
            //测量子view并且把Margin也作为子控件的一部分
            measureChildWithMargins(view, 0, 0);
            //宽度空隙 getWidth()得到Recycler控件的宽度,getDecoratedMeasuredWidth(view)拿到子view的宽度
            int widthSpace = getWidth() - getDecoratedMeasuredWidth(view);
            //高度空隙
            int heightSpace = getHeight() - getDecoratedMeasuredHeight(view);
            //给子view布局,这里居中了
            layoutDecoratedWithMargins(view, widthSpace / 2, 0,
                    widthSpace / 2 + getDecoratedMeasuredWidth(view),
                    getDecoratedMeasuredHeight(view) + 0);

            /**
             * 下面要调整每一层itemview的的大小及Y轴和Z轴的偏移
             * 最上面一层（第0层）的Scale为1,translationY为0
             * 依次往下,每层比上面一层:
             * (1)Scale相差0.05f
             * (2)translationY相差7dp
             * (3)translationZ相差1dp
             *
             * 注意:最后一层,除了水平方向的大小其他都与上一层一样，所以要特殊判断
             */
            if (position > 0) {  //大于0就是不是最上面那层
                //依次往下，每层都要水平方向缩小
                view.setScaleX(1 - CardConfig.SCALE_GAP * position);
                if (position < CardConfig.MAX_SHOW_COUNT - 1) {
                    //如果，不是最后一层，就都要调整
                    view.setScaleY(1 - CardConfig.SCALE_GAP * position); //垂直方向缩小
                    view.setTranslationY(CardConfig.TRANS_Y_GAP * position); //向下平移
                    view.setTranslationZ(CardConfig.TRANS_Z_GAP * (CardConfig.MAX_SHOW_COUNT - 1 - position));  //Z轴方向的平移
                } else {
                    //否则，就是最后一层，与上一层保持一致
                    view.setScaleY(1 - CardConfig.SCALE_GAP * (position - 1)); //垂直方向缩小
                    view.setTranslationY(CardConfig.TRANS_Y_GAP * (position - 1)); //向下平移
                    view.setTranslationZ(CardConfig.TRANS_Z_GAP * (CardConfig.MAX_SHOW_COUNT - 1 - (position - 1)));  //Z轴方向的平移
                }
            } else {
                //否则，是第0层(最上面那层),只需调整Z轴高度
                view.setTranslationZ(CardConfig.TRANS_Z_GAP * (CardConfig.MAX_SHOW_COUNT - 1));  //Z轴方向的平移
            }
        }
    }
}
