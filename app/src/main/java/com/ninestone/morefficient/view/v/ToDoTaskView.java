package com.ninestone.morefficient.view.v;

import com.ninestone.morefficient.model.TaskModel;

import java.util.List;

/**
 * 未完成任务View
 * Created by zhenglei on 2018/9/20.
 */
public interface ToDoTaskView {
    /**
     * 填充任务
     */
    void fillTask(List<TaskModel> tasks);
}
