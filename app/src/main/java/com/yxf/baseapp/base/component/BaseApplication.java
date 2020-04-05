package com.yxf.baseapp.base.component;

import android.app.Application;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
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
    private static BaseApplication context;
    private boolean isLogin = false;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        Density.setDensity(this, 660, 500);
        Logger.newBuilder()
                .tag("base_app")
                .showThreadInfo(false)
                .methodCount(1)
                .context(this)
                .saveFile(BuildConfig.DEBUG)
                .isDebug(BuildConfig.DEBUG)
                .build();

        initRefresh();
    }


    public static BaseApplication get() {
        return context;
    }

    /**
     * 判断app是否登录,需要项目的application传入，默认为false
     *
     * @return
     */
    public boolean isLogin() {
        return isLogin;
    }

    /***
     * 设置是否已登录，供外界调用
     * @param isLogin
     */
    public void setLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public void initRefresh() {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate));
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> new ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate));
    }
}
