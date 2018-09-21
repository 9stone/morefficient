package com.ninestone.morefficient.util;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;
import com.ninestone.morefficient.model.TaskModel;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by zhenglei on 2018/9/20.
 * 数据库配置工具类
 * 参考：https://stackoverflow.com/questions/17298773/android-studio-run-configuration-for-ormlite-config-generation/30222996#30222996
 */
public class DatabaseConfigUtil extends OrmLiteConfigUtil {
    private static final Class<?>[] classes = new Class[] {
            TaskModel.class
    };


    public static void main(String args[]) throws SQLException, IOException {
        writeConfigFile(new File("ormlite_config.txt"), classes);
    }
}
