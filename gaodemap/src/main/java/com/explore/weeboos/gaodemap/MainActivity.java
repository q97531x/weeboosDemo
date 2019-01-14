package com.explore.weeboos.gaodemap;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private View mView;
    private RecyclerView recyclerView;
    private FrameLayout content;
    private List<String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        content = findViewById(R.id.content);
        mView = LayoutInflater.from(this).inflate(R.layout.layout_scroll_view,null);
        recyclerView = mView.findViewById(R.id.recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new SimpleAdapter(this, initData()));

        Intent intent = new Intent(this, SlidingActivity.class);
        startActivity(intent);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        final FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(content.getWidth(), content.getHeight()/2);
        layoutParams.gravity = Gravity.BOTTOM;
        ValueAnimator slideAnimator = ValueAnimator.ofFloat(0.5f, 1.0f);
        slideAnimator.setRepeatCount(0);
        slideAnimator.setDuration(2000);
        slideAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (Float)valueAnimator.getAnimatedValue();
                layoutParams.height = (int)(content.getHeight() * value);
                mView.setLayoutParams(layoutParams);
            }
        });
        mView.setLayoutParams(layoutParams);
        content.addView(mView);
        slideAnimator.start();
    }

    private List<String> initData() {
        if(data == null) {
            data = new ArrayList<>();
        }
        for(int i = 0; i<10; i++) {
            data.add("12345678910\n position = " + i);
        }
        return data;
    }
}
