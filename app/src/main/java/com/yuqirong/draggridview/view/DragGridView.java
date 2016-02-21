package com.yuqirong.draggridview.view;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.yuqirong.draggridview.R;
import com.yuqirong.draggridview.adapter.DragGridAdapter;

/**
 * Created by Administrator on 2016/2/19.
 */
public class DragGridView extends GridView implements AdapterView.OnItemLongClickListener {

    private static final String TAG = "DragGridView";
    private WindowManager mWindowManager;

    private static final int MODE_DRAG = 1;
    private static final int MODE_NORMAL = 2;

    private int mode = MODE_NORMAL;
    private View view;
    private View dragView;
    // 要移动的item原先位置
    private int position;

    private int tempPosition;

    private WindowManager.LayoutParams layoutParams;
    // view的x差值
    private float mX;
    // view的y差值
    private float mY;
    // 手指按下时的x坐标(相对于整个屏幕)
    private float mWindowX;
    // 手指按下时的y坐标(相对于整个屏幕)
    private float mWindowY;

    public DragGridView(Context context) {
        this(context, null);
    }

    public DragGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        setOnItemLongClickListener(this);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mWindowX = ev.getRawX();
                mWindowY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (mode == MODE_DRAG) {
            return false;
        }
        this.view = view;
        this.position = position;
        this.tempPosition = position;
        mX = mWindowX - view.getLeft() - this.getLeft();
        mY = mWindowY - view.getTop() - this.getTop();
        initWindow();
        return true;
    }

    /**
     * 初始化window
     */
    private void initWindow() {
        if (dragView == null) {
            dragView = View.inflate(getContext(), R.layout.drag_item, null);
            TextView tv_text = (TextView) dragView.findViewById(R.id.tv_text);
            tv_text.setText(((TextView) view.findViewById(R.id.tv_text)).getText());
        }
        if (layoutParams == null) {
            layoutParams = new WindowManager.LayoutParams();
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            layoutParams.format = PixelFormat.RGBA_8888;
            layoutParams.gravity = Gravity.TOP | Gravity.LEFT;
            layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;  //悬浮窗的行为，比如说不可聚焦，非模态对话框等等
            layoutParams.width = view.getWidth();
            layoutParams.height = view.getHeight();
            layoutParams.x = view.getLeft() + this.getLeft();  //悬浮窗X的位置
            layoutParams.y = view.getTop() + this.getTop();  //悬浮窗Y的位置
            view.setVisibility(INVISIBLE);
        }

        mWindowManager.addView(dragView, layoutParams);
        mode = MODE_DRAG;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == MODE_DRAG) {
                    updateWindow(ev);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mode == MODE_DRAG) {
                    closeWindow(ev.getX(), ev.getY());
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 触摸移动时，window更新
     *
     * @param ev
     */
    private void updateWindow(MotionEvent ev) {
        if (mode == MODE_DRAG) {
            float x = ev.getRawX() - mX;
            float y = ev.getRawY() - mY;
            if (layoutParams != null) {
                layoutParams.x = (int) x;
                layoutParams.y = (int) y;
                mWindowManager.updateViewLayout(dragView, layoutParams);
            }
            float mx = ev.getX();
            float my = ev.getY();
            int dropPosition = pointToPosition((int) mx, (int) my);
            Log.i(TAG, "dropPosition : " + dropPosition + " , tempPosition : " + tempPosition);
            if (dropPosition == tempPosition || dropPosition == GridView.INVALID_POSITION) {
                return;
            }
            itemMove(dropPosition);
        }
    }

    /**
     * 判断item移动
     *
     * @param dropPosition
     */
    private void itemMove(int dropPosition) {
        TranslateAnimation translateAnimation;
        if (dropPosition < tempPosition) {
            for (int i = dropPosition; i < tempPosition; i++) {
                View view = getChildAt(i);
                View nextView = getChildAt(i + 1);
                float xValue = (nextView.getLeft() - view.getLeft()) * 1f / view.getWidth();
                float yValue = (nextView.getTop() - view.getTop()) * 1f / view.getHeight();
                translateAnimation =
                        new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, xValue, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, yValue);
                translateAnimation.setInterpolator(new LinearInterpolator());
                translateAnimation.setFillAfter(true);
                translateAnimation.setDuration(300);
                if (i == tempPosition - 1) {
                    translateAnimation.setAnimationListener(animationListener);
                }
                view.startAnimation(translateAnimation);
            }
        } else {
            for (int i = tempPosition + 1; i <= dropPosition; i++) {
                View view = getChildAt(i);
                View prevView = getChildAt(i - 1);
                float xValue = (prevView.getLeft() - view.getLeft()) * 1f / view.getWidth();
                float yValue = (prevView.getTop() - view.getTop()) * 1f / view.getHeight();
                translateAnimation =
                        new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, xValue, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, yValue);
                translateAnimation.setInterpolator(new LinearInterpolator());
                translateAnimation.setFillAfter(true);
                translateAnimation.setDuration(300);
                if (i == dropPosition) {
                    translateAnimation.setAnimationListener(animationListener);
                }
                view.startAnimation(translateAnimation);
            }
        }
        tempPosition = dropPosition;
    }

    /**
     * 动画监听器
     */
    Animation.AnimationListener animationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            // 在动画完成时将adapter里的数据交换位置
            ListAdapter adapter = getAdapter();
            if (adapter != null && adapter instanceof DragGridAdapter) {
                ((DragGridAdapter) adapter).exchangePosition(position, tempPosition, true);
            }
            position = tempPosition;
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    /**
     * 关闭window
     *
     * @param x
     * @param y
     */
    private void closeWindow(float x, float y) {
        if (dragView != null) {
            mWindowManager.removeView(dragView);
            dragView = null;
            layoutParams = null;
        }
        itemDrop();
        mode = MODE_NORMAL;
    }

    /**
     * 手指抬起时，item下落
     */
    private void itemDrop() {
        if (tempPosition == position || tempPosition == GridView.INVALID_POSITION) {
            getChildAt(position).setVisibility(VISIBLE);
        } else {
            ListAdapter adapter = getAdapter();
            if (adapter != null && adapter instanceof DragGridAdapter) {
                ((DragGridAdapter) adapter).exchangePosition(position, tempPosition, false);
            }
        }
    }

}
