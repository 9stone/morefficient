package com.ninestone.morefficient.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.ninestone.morefficient.R;
import com.ninestone.morefficient.model.TaskModel;
import com.ninestone.morefficient.persistent.DatabaseHelper;
import com.ninestone.morefficient.view.v.EditDetailTaskView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 编辑详细任务Presenter
 * Created by zhenglei on 2018/10/21.
 */
public class EditDetailTaskPresenter {
    private static final String TAG = "EditDetailTaskPresenter";

    private EditDetailTaskView mEditDetailTaskView;

    private DatabaseHelper mDatabaseHelper = null;
    private RuntimeExceptionDao<TaskModel, Long> mTaskDao;


    public EditDetailTaskPresenter(EditDetailTaskView editDetailTaskView) {
        this.mEditDetailTaskView = editDetailTaskView;
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
     * 更新信息任务
     * @param context
     * @param id
     * @param title
     * @param startTime
     * @param urgent
     * @return
     */
    public TaskModel updateDetailTask(Context context, long id, String title, long startTime, int urgent) {
        if (mTaskDao == null) {
            return null;
        }

        TaskModel taskModel = new TaskModel(id, title, startTime, 0, "", "", 0, urgent, 0, TaskModel.STATUS_TO_DO, 0);
        int updatedRows = mTaskDao.update(taskModel);

        if (updatedRows < 1) {
            Log.e(TAG, "TaskDao update fail, updatedRows:" + updatedRows);
            Toast.makeText(context, R.string.update_detail_task_fail, Toast.LENGTH_LONG).show();
            return null;
        }

        return taskModel;
    }

    /**
     * 查询并填充任务
     * @param id
     */
    public void query(long id) {
        if (mTaskDao == null) {
            return;
        }

        TaskModel taskModel = mTaskDao.queryForId(id);
        if (taskModel == null) {
            return;
        }

        if (mEditDetailTaskView != null) {
            mEditDetailTaskView.initTask(taskModel);
        }
    }

    /**
     * 格式化时间
     * @param time
     * @return
     */
    public static String formatTime(Calendar time) {
        if (time == null) {
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd HH时mm分");
        return sdf.format(time.getTime());
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
        if (databaseHelper == null) {
            return null;
        }

        return databaseHelper.getTaskDao();
    }

    private void releaseHelper() {
        if (mDatabaseHelper != null) {
            OpenHelperManager.releaseHelper();
            mDatabaseHelper = null;
        }
    }
}
