package com.explore.weeboos.gaodemap;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by weeboos
 * on 2018/11/30
 * 通过ViewDrag实现滑动上移效果的viewGroup
 */
public class SlidingLayout extends FrameLayout {

    private ViewDragHelper mDragger;
    private ViewDragHelper.Callback callback;
    private String slidingViewTag;

    public SlidingLayout(@NonNull Context context) {
        this(context,null);
    }

    public SlidingLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        callback = new DragCallBack();
        mDragger = ViewDragHelper.create(this,1.0f,callback);
    }

    public void addView(View view, String tag) {
        slidingViewTag = tag;
        view.setTag(tag);
        addView(view);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    /*@Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDragger.shouldInterceptTouchEvent(ev);
    }*/

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragger.processTouchEvent(event);
        return true;
    }

    class DragCallBack extends ViewDragHelper.Callback {
        @Override
        public boolean tryCaptureView(@NonNull View view, int i) {
            if(view.getTag()!=null && view.getTag().equals(slidingViewTag)) {
                return true;
            }
            return false;
        }

        @Override
        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
            return left;
        }

        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            return top;
        }
    }
}
