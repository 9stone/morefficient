package com.ninestone.morefficient.listener;

import android.os.Parcelable;

import com.ninestone.morefficient.model.TaskModel;

/**
 * 创建任务监听器
 * Created by zhenglei on 2018/9/29.
 */
public interface CreateTaskListener extends Parcelable {
    void onCreateTask(TaskModel taskModel);
}