package com.weeboos.view.easyrefreshlayout;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by weeboos
 * on 2019/1/4
 */
public class Utils {

    public static void removeViewFromParent(View view) {
        if (view == null) {
            return;
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
    }
}
