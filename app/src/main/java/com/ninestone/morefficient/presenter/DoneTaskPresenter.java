package com.ninestone.morefficient.presenter;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.ninestone.morefficient.model.TaskModel;
import com.ninestone.morefficient.persistent.DatabaseHelper;
import com.ninestone.morefficient.view.v.DoneTaskView;

import java.util.List;

/**
 * 已完成任务Presenter
 * Created by zhenglei on 2018/10/4.
 */
public class DoneTaskPresenter {
    private static final String TAG = "DoneTaskPresenter";

    private DoneTaskView mDoneTaskView;

    private DatabaseHelper mDatabaseHelper = null;
    private RuntimeExceptionDao<TaskModel, Integer> mTaskDao;


    public DoneTaskPresenter(DoneTaskView doneTaskView) {
        this.mDoneTaskView = doneTaskView;
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

        if (mDoneTaskView != null) {
            mDoneTaskView.fillTask(taskModels);
        }
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

        return mTaskDao.queryForEq(TaskModel.FIELD_STATUS, TaskModel.STATUS_DONE);
    }
}
