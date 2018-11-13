package com.ninestone.morefficient.view.v;

import com.ninestone.morefficient.model.TaskModel;

import java.util.List;

/**
 * 未完成任务View
 * Created by zhenglei on 2018/9/20.
 */
public interface ToDoTaskView {
    /**
     * 填充紧急任务
     * @param urgentTasks
     */
    void fillUrgentTask(List<TaskModel> urgentTasks);

    /**
     * 填充不紧急任务
     * @param noUrgentTasks
     */
    void fillNoUrgentTask(List<TaskModel> noUrgentTasks);

    /**
     * 填充任务数量
     * @param count
     */
    void fillCount(long count);
}
