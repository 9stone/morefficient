package com.ninestone.morefficient.application;

import android.app.Application;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;

/**
 * 全局Application
 */
public class MorefficientApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        init();
    }

    private void init() {
        initLeakCanary();
        initLogger();
    }

    /**
     * 内存泄漏监控
     */
    private void initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }

    /**
     * 日志
     */
    private void initLogger() {
        Logger.addLogAdapter(new AndroidLogAdapter());
    }
}
