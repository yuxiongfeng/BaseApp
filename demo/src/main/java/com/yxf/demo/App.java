package com.yxf.demo;

import android.app.Application;

import com.wms.logger.Logger;
import com.yxf.baseapp.base.component.BaseEntrance;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        BaseEntrance.getInstance()
                .init(this)//初始化
                .registerActivityListener(this)//注册activity的生命周期
                .initRefresh()//初始化SmartRefreshLayout的默认Header和Footer
                .setDensity(480, 667);//设置设计图上的尺寸

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


}
