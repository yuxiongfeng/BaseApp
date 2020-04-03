package com.yxf.baseapp.base.component;

import android.app.Application;

import com.wms.logger.Logger;
import com.yxf.baseapp.BuildConfig;
import com.yxf.baseapp.utils.Density;

/**
 * @Description: 基类application
 * @Author: yxf
 * @CreateDate: 2020/4/3 18:15
 * @UpdateUser: yxf
 * @UpdateDate: 2020/4/3 18:15
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Density.setDensity(this, 375, 667);
        Logger.newBuilder()
                .tag("base_app")
                .showThreadInfo(false)
                .methodCount(1)
                .context(this)
                .saveFile(BuildConfig.DEBUG)
                .isDebug(BuildConfig.DEBUG)
                .build();
    }
}
