package com.ninestone.morefficient.util;

import android.content.Context;
import android.util.DisplayMetrics;

import java.util.List;

/**
 * 公共工具类
 * Created by zhenglei on 2018/9/14.
 */
public class CommonUtil {

    /**
     * 判断List是否为空
     * @param list
     * @return
     */
    public static boolean isEmptyList(List<?> list) {
        return list==null || list.size()==0;
    }

    /**
     * 获取屏幕宽，单位像素
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        if (context == null) {
            throw new NullPointerException("context cannot be null");
        }

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        if (displayMetrics == null) {
            return 0;
        }

        return displayMetrics.widthPixels;
    }
}
