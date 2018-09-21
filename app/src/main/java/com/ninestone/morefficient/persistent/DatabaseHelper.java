package com.ninestone.morefficient.persistent;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.ninestone.morefficient.R;
import com.ninestone.morefficient.model.TaskModel;

import java.sql.SQLException;

/**
 * 数据库助手类
 * Created by zhenglei on 2018/9/20.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "morefficient.db";
    private static final int DATABASE_VERSION = 1;

    private RuntimeExceptionDao<TaskModel, Integer> mRuntimeExceptionTaskDao = null;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }

    @Override
    public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {
        Log.i(DatabaseHelper.class.getName(), "onCreate");

        try {
            TableUtils.createTable(connectionSource, TaskModel.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        Log.i(DatabaseHelper.class.getName(), "onUpgrade, oldVersion:" + oldVersion + ", newVersion:" + newVersion);
    }

    @Override
    public void close() {
        super.close();
        mRuntimeExceptionTaskDao = null;
    }

    public RuntimeExceptionDao<TaskModel, Integer> getTaskDao() {
        if(mRuntimeExceptionTaskDao == null) {
            mRuntimeExceptionTaskDao = getRuntimeExceptionDao(TaskModel.class);
        }

        return mRuntimeExceptionTaskDao;
    }
}
