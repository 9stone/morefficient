package com.ninestone.morefficient.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.ninestone.morefficient.R;
import com.ninestone.morefficient.model.TaskModel;
import com.ninestone.morefficient.persistent.DatabaseHelper;
import com.ninestone.morefficient.view.v.CreateTaskView;

/**
 * 创建任务Presenter
 * Created by zhenglei on 2018/9/25.
 */
public class CreateTaskPresenter {
    private static final String TAG = "CreateTaskPresenter";

    private CreateTaskView mCreateTaskView;

    private DatabaseHelper mDatabaseHelper = null;
    private RuntimeExceptionDao<TaskModel, Integer> mTaskDao;


    public CreateTaskPresenter(CreateTaskView createTaskView) {
        this.mCreateTaskView = createTaskView;
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
     * 创建任务
     * @param context
     * @param summary
     * @return
     */
    public TaskModel createTask(Context context, String summary) {
        if (TextUtils.isEmpty(summary)) {
            throw new IllegalArgumentException("summary cannot be empty");
        }

        if (mTaskDao == null) {
            return null;
        }

        TaskModel taskModel = new TaskModel(summary, 0, 0, "", "", 0, 0, 0, 0);
        int updatedRows = mTaskDao.create(taskModel);

        if (updatedRows < 1) {
            Log.e(TAG, "TaskDao create fail, updatedRows:" + updatedRows);
            if (context != null) {
                Toast.makeText(context, R.string.create_task_fail, Toast.LENGTH_LONG).show();
            }
            return null;
        }

        return taskModel;
    }

    private DatabaseHelper getHelper(Context context) {
        if (context == null) {
            throw new NullPointerException("context cannot be null");
        }

        if(mDatabaseHelper == null) {
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
}
