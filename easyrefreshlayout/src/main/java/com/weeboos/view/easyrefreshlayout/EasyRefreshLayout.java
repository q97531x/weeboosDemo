package com.weeboos.view.easyrefreshlayout;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Message;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.OverScroller;

/**
 * Created by weeboos
 * on 2018/12/10
 */
public class EasyRefreshLayout extends LinearLayout implements NestedScrollingParent {
    private final String TAG = this.getClass().getSimpleName();

    private NestedScrollingParentHelper mNestedParentHelper;

    private View mTarget;

    private View mHeaderView;

    private View mFooterView;

    private int totalHeaderY = -1, totalFooterY = -1;

    private int mHeaderHeight, mFooterHeight;

    private boolean isRefresh = false;

    private boolean isLoad = false;

    private boolean isAnim = false;

    private OverScroller mScroller;

    private RefreshHandler mHandler;

    private ValueAnimator valueAnimator;

    private RefreshListener refreshListener;

    public EasyRefreshLayout(Context context) {
        this(context,null);
    }

    public EasyRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    @SuppressLint("HandlerLeak")
    public EasyRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mNestedParentHelper = new NestedScrollingParentHelper(this);
        mScroller = new OverScroller(context);
        mHandler = new RefreshHandler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case REFRESH_CODE:
                        Log.d("threadCode", "wake up" + System.currentTimeMillis());
                        refreshCompleteAnim();
                        break;
                    case LOAD_CODE:

                        break;
                }
                super.handleMessage(msg);
            }
        };
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int childCount = getChildCount();
        int finalHeight = 0;
        final int paddingLeft = getPaddingLeft();
        final int paddingRight = getPaddingRight();
        final int paddingTop = getPaddingTop();
        final int paddingBottom = getPaddingBottom();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) child.getLayoutParams();
            int childWidthSpec = MeasureSpec.makeMeasureSpec(width - lp.leftMargin - lp.rightMargin - paddingLeft - paddingRight, MeasureSpec.EXACTLY);
            int childHeightSpec = getChildMeasureSpec(heightMeasureSpec,
                    paddingTop + paddingBottom + lp.topMargin + lp.bottomMargin, lp.height);
            child.measure(childWidthSpec, childHeightSpec);
            finalHeight += child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        Log.d(TAG,"layout---" + "l:"+ l + "t:" + t+"r:" + r+ "b" + b);
        if(mHeaderView != null) {
            mHeaderView.layout(l, t - mHeaderView.getHeight(), r, t);
            mHeaderHeight = mHeaderView.getHeight();
            Log.d("layout",  "head" + mHeaderView.getHeight());
        }

        if(mFooterView != null) {
            mFooterView.layout(l, b, r, b + mFooterView.getHeight());
            mFooterHeight = mFooterView.getHeight();
            Log.d("layout",  "foot" + mFooterView.getHeight());
        }
        getChildAt(1).layout(l,t,r,b);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    public View getHeaderView() {
        return mHeaderView;
    }

    public void setHeaderView(View headerView) {
        /*removeViewAt(0);
        addView(headerView, 0);*/
        mHeaderView = headerView;
        delAddHeaderView();
    }

    private void delAddHeaderView() {
        if(indexOfChild(mHeaderView) == -1) {
            Utils.removeViewFromParent(mHeaderView);
            addView(mHeaderView, 0);
        }
    }

    public View getFooterView() {
        return mFooterView;
    }

    public void setFooterView(View footerView) {
        mFooterView = footerView;
        delAddFooterView();
    }

    private void delAddFooterView() {
        if(indexOfChild(mFooterView) == -1) {
            Utils.removeViewFromParent(mFooterView);
            addView(mFooterView, 2);
        }
    }

    public void setRefreshListener(RefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }

    //刷新结束动画
    private void refreshCompleteAnim() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1.0f, 0.0f);
        valueAnimator.setRepeatCount(0);
        valueAnimator.setDuration(200);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int y = (int) (getScaleY() * (float) animation.getAnimatedValue());
                scrollTo(0, y);
//                    Log.d(TAG, "value = " + y + "lastValue = " + animation.getAnimatedValue());
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isRefresh = false;
                totalHeaderY = 0;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.start();
    }

    private void loadCompleteAnim() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1.0f, 0.0f);
        valueAnimator.setRepeatCount(0);
        valueAnimator.setDuration(500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int y = (int) (totalFooterY * (float) animation.getAnimatedValue());
                scrollTo(0, y);
//                    Log.d(TAG, "value = " + y + "lastValue = " + animation.getAnimatedValue());
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isLoad = false;
                totalFooterY = 0;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.start();
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        boolean startScroll = (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
//        Log.i(TAG,"onStartNestedScroll--"+ startScroll + isRefresh);
        //竖直方向滑动
        return startScroll;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        mNestedParentHelper.onNestedScrollAccepted(child, target, axes);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {

        boolean hiddenTop = dy > 0 && getScrollY() < mHeaderHeight;
        boolean showTop = dy < 0 && !ViewCompat.canScrollVertically(target, -1);

        boolean showBottom = dy > 0 && !ViewCompat.canScrollVertically(target, 1);
        boolean hiddenBottom = dy < 0 && getScrollY() > 0;
        Log.d(TAG, "dy = " + dy);
        Log.d(TAG, "hiddenTop = " + hiddenTop + "showTop = " + showTop);
//        Log.d(TAG, "hiddenBottom = " + hiddenBottom + "showBottom = " + showBottom);
        Log.d(TAG, "scrollY = " + getScrollY());
        if(hiddenTop || showTop) {
            Log.d(TAG, "head");
            scrollBy(0, dy);
            consumed[1] = dy;
        }
        /*if(showBottom || hiddenBottom) {
            Log.d(TAG, "load");
            scrollBy(0, dy);
            consumed[1] = dy;
        }*/

        /*if(dy<0){
            //手指向下滑动
            if(isLoad) {
                if (totalFooterY > 0) {
                    totalFooterY -= Math.abs(dy);
                    scrollBy(0, dy);
                    consumed[1] = dy;
                } else {
                    totalFooterY = 0;
                    scrollBy(0, 0);
                    if (valueAnimator != null && valueAnimator.isRunning()) {
                        valueAnimator.cancel();
                    }
                }
            }else {
                if(!ViewCompat.canScrollVertically(target,-1)) {
                    //不在刷新状态
                    if(totalHeaderY >= mHeaderView.getHeight()) {
                        Log.d(TAG, "refresh");
                        //去除部分误差
                        totalHeaderY = mHeaderView.getHeight();
                        isRefresh = true;
                        //延迟一秒
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("thread", "sleep" + System.currentTimeMillis());
                                mHandler.sendEmptyMessage(RefreshHandler.REFRESH_CODE);
                            }
                        },1000);
                    }else {
                        Log.d(TAG, "scroll" + "isRefresh = " + !isRefresh + "canScroll = " + !ViewCompat.canScrollVertically(target,-1));
                        totalHeaderY += Math.abs(dy);
                        scrollBy(0, dy);
                        consumed[1] = dy;
                    }
                }else {
                    scrollBy(0, 0);
                }
            }
        }else {
            //手指向上滑动
            if (totalHeaderY > 0) {
                totalHeaderY -= Math.abs(dy);
                scrollBy(0, dy);
                consumed[1] = dy;
            } else {
                totalHeaderY = 0;
                scrollBy(0, 0);
                if(valueAnimator!=null && valueAnimator.isRunning()) {
                    valueAnimator.cancel();
                }
            }
            if(!target.canScrollVertically(1) && totalHeaderY == 0) {
                if(totalFooterY >= mFooterView.getHeight()) {
                    //去除部分误差
                    totalFooterY = mFooterView.getHeight();
                    isLoad = true;
                    //延迟一秒
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("thread", "sleep" + System.currentTimeMillis());
                            mHandler.sendEmptyMessage(RefreshHandler.REFRESH_CODE);
                        }
                    },1000);
                }else {
                    totalFooterY += Math.abs(dy);
                    scrollBy(0, dy);
                    consumed[1] = dy;
                }
            }else {
                scrollBy(0, 0);
            }
        }*/

//        Log.i(TAG,"onNestedPreScroll--" + "canScrollDown = "+ ViewCompat.canScrollVertically(target,1) + "dy = " + dy + "totalHeaderY" + totalHeaderY);
    }

    public void completeRefresh() {
        refreshCompleteAnim();
    }

    public void completeLoading() {

    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
//        Log.i(TAG,"onNestedFling: target = [" + target.getClass().getName() + "], velocityX = [" + velocityX + "], velocityY = [" + velocityY + "], consumed = [" + consumed + "]");
        if (getScrollY() >= getHeaderView().getHeight()) return false;

        fling((int) velocityY);
        return true;
    }

    public void fling(int velocityY)
    {
        if(velocityY < 0) {
            mScroller.fling(0, getScrollY(), 0, velocityY, 0, 0, 0, mHeaderHeight);
        }else if(velocityY > 0){
            mScroller.fling(0, getScrollY(), 0, velocityY, 0, 0, 0, mFooterHeight);
        }
        invalidate();
    }

    @Override
    public void scrollTo(int x, int y)
    {
        if (y < 0) {
            y = Math.abs(y);
            if (y > mHeaderHeight)
            {
                y = mHeaderHeight;
                //延迟一秒
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("thread", "sleep" + System.currentTimeMillis());
                        mHandler.sendEmptyMessage(RefreshHandler.REFRESH_CODE);
                    }
                },1000);
            }
            if (y != getScrollY())
            {
                super.scrollTo(x, -y);
            }
        }else {
            if(y > mFooterHeight) {
                y = mFooterHeight;
            }
            if (y != getScrollY())
            {
                super.scrollTo(x, y);
            }
        }

    }

    @Override
    public void onStopNestedScroll(View target) {
//        Log.i(TAG,"======== onStopNestedScroll =======");
        mNestedParentHelper.onStopNestedScroll(target);
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return false;
    }
}
