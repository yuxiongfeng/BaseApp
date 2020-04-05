package com.yxf.demo;

import android.app.Application;

import com.wms.logger.Logger;
import com.yxf.baseapp.base.component.BaseEntrance;


public class App extends Application {
    private boolean mIsLogin;

    @Override
    public void onCreate() {
        super.onCreate();

        BaseEntrance.getInstance()
                .init(this)
                .initRefresh()
                .setDensity(375,667);

             Logger.newBuilder()
                .tag("demo:")
                .showThreadInfo(false)
                .methodCount(1)
                .saveLogCount(7)
                .context(this)
                .deleteOnLaunch(false)
                .saveFile(BuildConfig.DEBUG)
                .isDebug(BuildConfig.DEBUG)
                .build();

    }

    private void setLogin(boolean mIsLogin) {
        this.mIsLogin = mIsLogin;
        BaseEntrance.getInstance().setLogin(mIsLogin);
    }
}
