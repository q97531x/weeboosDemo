package com.explore.weeboos.demo;

import android.content.Context;

import java.util.List;

/**
 * Created by weeboos
 * on 2018/12/10
 */
public class SimpleStringAdapter extends CommonAdapter<String> {

    public SimpleStringAdapter(List<String> data, Context context, int layoutId) {
        super(data, context, layoutId);
    }

    @Override
    public void convertViewHolder(CommonViewHolder holder, int position) {
        holder.setText(R.id.tv_title, data.get(position));
    }
}
