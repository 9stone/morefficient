package com.ninestone.morefficient.view;

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
}
