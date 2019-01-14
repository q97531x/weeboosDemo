package com.explore.weeboos.demo;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import com.weeboos.view.easyrefreshlayout.EasyRefreshLayout;
import com.weeboos.view.easyrefreshlayout.RefreshListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EasyRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private SimpleStringAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        refreshLayout = findViewById(R.id.easy_refresh_layout);
        recyclerView = findViewById(R.id.recycler);
        adapter = new SimpleStringAdapter(initData(), this, R.layout.demo_item_simple_string);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        refreshLayout.setHeaderView(LayoutInflater.from(this).inflate(R.layout.demo_layout_header_view, null));
        refreshLayout.setFooterView(LayoutInflater.from(this).inflate(R.layout.demo_layout_header_view, null));
        refreshLayout.setRefreshListener(new RefreshListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoad() {

            }
        });
    }

    private List<String> initData() {
        List<String> data = new ArrayList<>();
        for(int i=0; i<10; i++) {
            data.add("" + i);
        }
        return data;
    }

}
