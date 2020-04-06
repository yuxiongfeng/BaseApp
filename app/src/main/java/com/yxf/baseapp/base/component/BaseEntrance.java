package com.yxf.baseapp.base.component;

import android.app.Application;
import android.content.Context;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.wms.logger.Logger;
import com.yxf.baseapp.BuildConfig;
import com.yxf.baseapp.utils.ActivityManager;
import com.yxf.baseapp.utils.Density;


public class BaseEntrance {

    private Context mContext;
    private boolean mIsLogin = false;

    /**
     * 获取BaseEntrance实例
     *
     * @return
     */
    public static BaseEntrance getInstance() {
        return Inner.instance;
    }

    private static final class Inner {
        private static final BaseEntrance instance = new BaseEntrance();
    }

    /**
     * 必须先初始化，供外界调用（必须要调用）
     *
     * @param context
     */
    public BaseEntrance init(Context context) {
        Logger.newBuilder()
                .tag("baseApp_log")
                .showThreadInfo(false)
                .methodCount(1)
                .saveLogCount(7)
                .context(context)
                .deleteOnLaunch(false)
                .saveFile(BuildConfig.DEBUG)
                .isDebug(BuildConfig.DEBUG)
                .build();
        mContext = context;
        return this;
    }

    /**
     * 注册activity的生命周期(必须要调用)
     *
     * @param application
     * @return
     */
    public BaseEntrance registerActivityListener(Application application) {
        ActivityManager.registerActivityListener(application);
        return this;
    }

    public Context get() {
        return mContext;
    }

    /**
     * 判断app是否登录,需要项目的application传入，默认为false
     *
     * @return
     */
    public boolean isLogin() {
        return mIsLogin;
    }

    /***
     * 设置是否已登录，供外界调用
     * @param isLogin
     */
    public BaseEntrance setLogin(boolean isLogin) {
        mIsLogin = isLogin;
        return this;
    }

    public BaseEntrance initRefresh() {
        if (mContext == null) {
            throw new NullPointerException("context is null,please init first!");
        }
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate));
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> new ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate));
        return this;
    }

    public BaseEntrance setDensity(int designWidth, int designHeight) {
        if (mContext == null) {
            throw new NullPointerException("application is null,please init first!");
        }
        Density.setDensity((Application) mContext, designWidth, designHeight);
        return this;
    }


}
