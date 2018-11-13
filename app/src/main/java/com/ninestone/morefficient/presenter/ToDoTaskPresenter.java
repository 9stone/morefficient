package com.ninestone.morefficient.presenter;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.ninestone.morefficient.model.TaskModel;
import com.ninestone.morefficient.persistent.DatabaseHelper;
import com.ninestone.morefficient.view.v.ToDoTaskView;

import java.sql.SQLException;
import java.util.List;

/**
 * 未完成任务Presenter
 * Created by zhenglei on 2018/9/20.
 */
public class ToDoTaskPresenter {
    private static final String TAG = "ToDoTaskPresenter";

    private ToDoTaskView mToDoTaskView;

    private DatabaseHelper mDatabaseHelper = null;
    private RuntimeExceptionDao<TaskModel, Long> mTaskDao;


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
     * 获取所有任务列表
     */
    public void getAllTask() {
        getUrgentTask();
        getNoUrgentTask();
        getTaskCount();
    }

    /**
     * 移除任务
     * @param taskModel
     * @return
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
     * 获取紧急任务列表
     */
    private void getUrgentTask() {
        if (mTaskDao == null) {
            return;
        }

        List<TaskModel> urgentTasks = queryUrgentTask();

        if (mToDoTaskView != null) {
            mToDoTaskView.fillUrgentTask(urgentTasks);
        }
    }

    /**
     * 获取不紧急任务列表
     */
    private void getNoUrgentTask() {
        if (mTaskDao == null) {
            return;
        }

        List<TaskModel> noUrgentTasks = queryNoUrgentTask();

        if (mToDoTaskView != null) {
            mToDoTaskView.fillNoUrgentTask(noUrgentTasks);
        }
    }

    /**
     * 获取任务数量
     */
    private void getTaskCount() {
        if (mTaskDao == null) {
            return;
        }

        long count = queryCount();

        if (mToDoTaskView != null) {
            mToDoTaskView.fillCount(count);
        }
    }

    private List<TaskModel> queryUrgentTask() {
        if (mTaskDao == null) {
            return null;
        }

        QueryBuilder<TaskModel, Long> queryBuilder = mTaskDao.queryBuilder();
        if (queryBuilder == null) {
            return null;
        }

        Where<TaskModel, Long> where = queryBuilder.where();
        if (where == null) {
            return null;
        }

        try {
            where.eq(TaskModel.FIELD_STATUS, TaskModel.STATUS_TO_DO);
            where.and();
            where.eq(TaskModel.FIELD_URGENT, TaskModel.URGENT);
            return queryBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private List<TaskModel> queryNoUrgentTask() {
        if (mTaskDao == null) {
            return null;
        }

        QueryBuilder<TaskModel, Long> queryBuilder = mTaskDao.queryBuilder();
        if (queryBuilder == null) {
            return null;
        }

        Where<TaskModel, Long> where = queryBuilder.where();
        if (where == null) {
            return null;
        }

        try {
            where.eq(TaskModel.FIELD_STATUS, TaskModel.STATUS_TO_DO);
            where.and();
            where.eq(TaskModel.FIELD_URGENT, TaskModel.NO_URGENT);
            return queryBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
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
                    .eq(TaskModel.FIELD_STATUS, TaskModel.STATUS_TO_DO)
                    .countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0L;
    }
}
