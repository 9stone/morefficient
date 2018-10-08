package com.ninestone.morefficient.presenter;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.ninestone.morefficient.model.TaskModel;
import com.ninestone.morefficient.persistent.DatabaseHelper;
import com.ninestone.morefficient.view.v.ToDoTaskView;

import java.util.List;

/**
 * 未完成任务Presenter
 * Created by zhenglei on 2018/9/20.
 */
public class ToDoTaskPresenter {
    private static final String TAG = "ToDoTaskPresenter";

    private ToDoTaskView mToDoTaskView;

    private DatabaseHelper mDatabaseHelper = null;
    private RuntimeExceptionDao<TaskModel, Integer> mTaskDao;


    public ToDoTaskPresenter(ToDoTaskView toDoTaskView) {
        this.mToDoTaskView = toDoTaskView;
    }

    /**
     * 创建
     * @param context
     */
    public void create(Context context) {
        mDatabaseHelper = getHelper(context);
        mTaskDao = getTaskDao(mDatabaseHelper);
    }

    /**
     * 销毁
     */
    public void destroy() {
        releaseHelper();
    }

    /**
     * 获取任务列表
     */
    public void getTask() {
        if (mTaskDao == null) {
            return;
        }

        List<TaskModel> taskModels = queryTask();

        if (mToDoTaskView != null) {
            mToDoTaskView.fillTask(taskModels);

            int count = taskModels == null
                                    ? 0
                                    : taskModels.size();
            mToDoTaskView.fillCount(count);
        }
    }

    /**
     * 移除任务
     * @param taskModel
     */
    public boolean remove(TaskModel taskModel) {
        if (taskModel == null) {
            throw new NullPointerException("taskModel cannot be null");
        }

        taskModel.setStatus(TaskModel.STATUS_DONE);

        if (mTaskDao == null) {
            return false;
        }

        int updatedRows = mTaskDao.update(taskModel);
        if (updatedRows <= 0) {
            Log.w(TAG, "updatedRows <= 0");
            return false;
        }

        List<TaskModel> taskModels = queryTask();

        if (mToDoTaskView != null) {
            mToDoTaskView.fillTask(taskModels);
        }

        return true;
    }

    private DatabaseHelper getHelper(Context context) {
        if (context == null) {
            throw new NullPointerException("context cannot be null");
        }

        if (mDatabaseHelper == null) {
            mDatabaseHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
        }
        return mDatabaseHelper;
    }

    private RuntimeExceptionDao<TaskModel, Integer> getTaskDao(DatabaseHelper databaseHelper) {
        if(databaseHelper == null) {
            return null;
        }

        return databaseHelper.getTaskDao();
    }

    private void releaseHelper() {
        if(mDatabaseHelper != null) {
            OpenHelperManager.releaseHelper();
            mDatabaseHelper = null;
        }
    }

    private List<TaskModel> queryTask() {
        if (mTaskDao == null) {
            return null;
        }

        return mTaskDao.queryForEq(TaskModel.FIELD_STATUS, TaskModel.STATUS_TO_DO);
    }
}
