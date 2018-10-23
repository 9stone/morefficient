package com.ninestone.morefficient.listener;

import android.os.Parcelable;

import com.ninestone.morefficient.model.TaskModel;

/**
 * 编辑任务监听器
 * Created by zhenglei on 2018/10/21.
 */
public interface EditTaskListener extends Parcelable {
    void onEditTask(TaskModel taskModel);
}
