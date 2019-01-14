package com.explore.weeboos.demo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by weeboos
 * on 2018/11/20
 * RecycleView的通用Adapter
 */
public abstract class CommonAdapter<T> extends RecyclerView.Adapter<CommonViewHolder> {

    protected List<T> data;

    protected Context context;

    private int layoutId;

    protected LayoutInflater inflater;

    public CommonAdapter(List<T> data, Context context, int layoutId) {
        this.data = data;
        this.context = context;
        this.layoutId = layoutId;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(layoutId, parent,false);
        return new CommonViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CommonViewHolder holder, int position) {
        convertViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public abstract void convertViewHolder(CommonViewHolder holder, int position);

    public void setData(List<T> list) {
        if(data != null) {
            if(data.size() > 0) {
                data.clear();
            }
            data.addAll(list);
        }else {
            data = list;
        }
        notifyDataSetChanged();
    }
}
