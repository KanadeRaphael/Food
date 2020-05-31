package com.example.food.config;

import android.graphics.Canvas;
import android.os.Build;
import android.util.Log;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food.adapter.CardAdapter;
import com.example.food.adapter.CardHolder;

import java.util.List;

public class CardItemTouchCallBack extends ItemTouchHelper.Callback {

    private static final String TAG = "CardItemTouchCallBack";
    private RecyclerView recyclerView;
    private CardAdapter adapter;
    private List datas;

    public CardItemTouchCallBack(RecyclerView recyclerView, CardAdapter adapter, List datas) {
        this.recyclerView = recyclerView;
        this.adapter = adapter;
        this.datas = datas;
    }

    /**
     * 是否开启长按拖拽
     * true，开启
     * false,不开启长按退拽
     */
    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    /**
     * 是否开启滑动
     * true，开启
     * false,不开启长按退拽
     */
    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    /**
     * ItemTouchHelper支持设置事件方向，并且必须重写当前getMovementFlags来指定支持的方向
     * dragFlags  表示拖拽的方向，有六个类型的值：LEFT、RIGHT、START、END、UP、DOWN，0表示不监听
     * swipeFlags 表示滑动的方向，有六个类型的值：LEFT、RIGHT、START、END、UP、DOWN
     * 最后要通过makeMovementFlags（dragFlag，swipe）创建方向的Flag
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        return makeMovementFlags(0, swipeFlags);
    }

    /**
     * 长按item拖动到其他item时触发
     */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    /**
     * item滑走(飞出屏幕)时触发
     *
     * @param viewHolder 滑动的viewholder
     * @param direction  滑动的方向
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        //这里来判断画出的方向,左边是4,右边是8,然后可以做一些数据操作
        Log.d(TAG, "onSwiped: " + direction);
        switch (direction) {
            case 4:
                Log.d(TAG, "onSwiped: 左边滑出");
                adapter.swipeLeft();
                break;
            case 8:
                Log.d(TAG, "onSwiped: 右边滑出");
                adapter.swipeRight();
                break;
        }
        //移除这条数据
        Object remove = datas.remove(viewHolder.getLayoutPosition());

        /** 这个位置可以用来加载数据,当滑到还剩4个或者多少个时可以在后面加载数据，添加到datas中*/
        //这里就为了方便，直接循环了，把移除的元素再添加到末尾
        datas.add(datas.size(), remove);

        //刷新
        adapter.notifyDataSetChanged();
        //复位
        viewHolder.itemView.setRotation(0);
        if (viewHolder instanceof CardHolder) {
            CardHolder holder = (CardHolder) viewHolder;
        }
    }

    /**
     * 拖动时触发
     *
     * @param c Canvas
     * @param recyclerView RecyclerView
     * @param viewHolder ViewHolder
     * @param dX float
     * @param dY float
     * @param actionState int
     * @param isCurrentlyActive boolean
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        double swipeValue = Math.sqrt(dX * dX + dY * dY);   //滑动离中心的距离
        double fraction = swipeValue / (recyclerView.getWidth() * 0.5f);
        //边界修正 最大为1
        if (fraction > 1) {
            fraction = 1;
        }

        //调整每个子view的缩放、位移之类的
        int childCount = recyclerView.getChildCount();  //拿到子view的数量
        isUpOrDown(recyclerView.getChildAt(childCount - 1));
        for (int i = 0; i < childCount; i++) {
            View childView = recyclerView.getChildAt(i);    //最底层view的i=0
            int level = childCount - i - 1;  //转换一下,level代表层数,最上面是第0层
            if (level > 0) {
                //每一层的水平方向都要增大
                childView.setScaleX((float) (1 - CardConfig.SCALE_GAP * level + fraction * CardConfig.SCALE_GAP));
                if (level < CardConfig.MAX_SHOW_COUNT - 1) {
                    //1 2层
                    childView.setScaleY((float) (1 - CardConfig.SCALE_GAP * level + fraction * CardConfig.SCALE_GAP));
                    childView.setTranslationY((float) (CardConfig.TRANS_Y_GAP * level - fraction * CardConfig.TRANS_Y_GAP));
                    childView.setTranslationZ((float) (CardConfig.TRANS_Z_GAP * (CardConfig.MAX_SHOW_COUNT - 1 - level)
                            + fraction * CardConfig.TRANS_Z_GAP));
                } else {
                    //最下面一层,3层,这层不用变,所以这里不用写
                }
            } else {
                //第0层
                //拿到水平方向的偏移比率
                float xFraction = dX / (recyclerView.getWidth() * 0.5f);
                //边界修正,有正有负，因为旋转有两个方向
                if (xFraction > 1) {
                    xFraction = 1;
                } else if (xFraction < -1) {
                    xFraction = -1;
                }
                childView.setRotation(xFraction * 15);  //第一层滑动的时候稍微有点旋转，这但最多旋转15度

                if (viewHolder instanceof CardHolder) {
                    CardHolder holder = (CardHolder) viewHolder;
                }
            }
        }
    }

    @Override
    public float getSwipeThreshold(RecyclerView.ViewHolder viewHolder) {
        return super.getSwipeThreshold(viewHolder);
    }

    @Override
    public float getSwipeEscapeVelocity(float defaultValue) {
        return super.getSwipeEscapeVelocity(defaultValue);
    }

    @Override
    public float getSwipeVelocityThreshold(float defaultValue) {
        Log.d(TAG, "getSwipeVelocityThreshold: " + defaultValue);
        View topView = recyclerView.getChildAt(recyclerView.getChildCount() - 1);
        if (isUpOrDown(topView)) { //如果是向上或者向下滑动
            return Float.MAX_VALUE; //就返回阈值为很大
        }
        return super.getSwipeVelocityThreshold(defaultValue);
    }

    /**
     * 判断是否是向上滑或者向下滑
     */
    private boolean isUpOrDown(View topView) {
        float x = topView.getX();
        float y = topView.getY();
        int left = topView.getLeft();
        int top = topView.getTop();
        return !(Math.pow(x - left, 2) > Math.pow(y - top, 2));
    }
}
