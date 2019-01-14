package com.explore.weeboos.gaodemap;

import android.animation.ValueAnimator;
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

public class SlidingActivity extends AppCompatActivity {

    private View mView;
    private RecyclerView recyclerView;
    private List<String> data;
    private SlidingLayout content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sliding);
        getSupportFragmentManager().beginTransaction().replace(R.id.content, ContainerFragment.newInstance("c","c")).commit();
//        mView = LayoutInflater.from(this).inflate(R.layout.layout_scroll_view,null);
//        recyclerView = mView.findViewById(R.id.recycle);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(new SimpleAdapter(this, initData()));

//        content.addView(mView,"sliding");

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
