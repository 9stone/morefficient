package com.ninestone.morefficient.presenter;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.ninestone.morefficient.model.TaskModel;
import com.ninestone.morefficient.persistent.DatabaseHelper;
import com.ninestone.morefficient.view.v.DoneTaskView;

import java.sql.SQLException;
import java.util.List;

/**
 * 已完成任务Presenter
 * Created by zhenglei on 2018/10/4.
 */
public class DoneTaskPresenter {
    private static final String TAG = "DoneTaskPresenter";

    private DoneTaskView mDoneTaskView;

    private DatabaseHelper mDatabaseHelper = null;
    private RuntimeExceptionDao<TaskModel, Long> mTaskDao;


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
     * 获取所有任务列表
     */
    public void getAllTask() {
        getTask();
        getTaskCount();
    }

    /**
     * 恢复任务，将在未完成任务列表中重新显示
     * @param taskModel
     * @return
     */
    public boolean revert(TaskModel taskModel) {
        if (taskModel == null) {
            throw new NullPointerException("taskModel cannot be null");
        }

        taskModel.setStatus(TaskModel.STATUS_TO_DO);

        if (mTaskDao == null) {
            return false;
        }

        int updatedRows = mTaskDao.update(taskModel);
        if (updatedRows <= 0) {
            Log.w(TAG, "updatedRows <= 0");
            return false;
        }

        getAllTask();

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

    private RuntimeExceptionDao<TaskModel, Long> getTaskDao(DatabaseHelper databaseHelper) {
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

    /**
     * 获取任务
     */
    private void getTask() {
        if (mTaskDao == null) {
            return;
        }

        List<TaskModel> taskModels = queryTask();

        if (mDoneTaskView != null) {
            mDoneTaskView.fillTask(taskModels);
        }
    }

    private void getTaskCount() {
        if (mTaskDao == null) {
            return;
        }

        long count = queryCount();

        if (mDoneTaskView != null) {
            mDoneTaskView.fillCount(count);
        }
    }

    private List<TaskModel> queryTask() {
        if (mTaskDao == null) {
            return null;
        }

        return mTaskDao.queryForEq(TaskModel.FIELD_STATUS, TaskModel.STATUS_DONE);
    }

    private long queryCount() {
        if (mTaskDao == null) {
            return 0L;
        }

        QueryBuilder<TaskModel, Long> queryBuilder = mTaskDao.queryBuilder();
        if (queryBuilder == null) {
            return 0L;
        }

        Where<TaskModel, Long> where = queryBuilder.where();
        if (where == null) {
            return 0L;
        }

        try {
            return where
                    .eq(TaskModel.FIELD_STATUS, TaskModel.STATUS_DONE)
                    .countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0L;
    }
}
