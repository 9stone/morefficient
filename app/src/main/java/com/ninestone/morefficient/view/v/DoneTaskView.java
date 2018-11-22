package com.ninestone.morefficient.view.v;

import com.ninestone.morefficient.model.TaskModel;

import java.util.List;

/**
 * 已完成任务View
 * Created by zhenglei on 2018/10/4.
 */
public interface DoneTaskView {
    /**
     * 填充任务
     */
    void fillTask(List<TaskModel> tasks);

    /**
     * 填充任务数量
     * @param count
     */
    void fillCount(long count);
}
