package com.yxf.baseapp.utils;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.wms.logger.Logger;


/**
 * 适配相关
 */
public class Density {

    private static float appDensity;
    private static float appScaledDensity;
    private static DisplayMetrics appDisplayMetrics;
    private static int barHeight;
    private static float designWidth, designHeight;

    public static void setDensity(@NonNull final Application application, int designWidth, int designHeight) {
        //获取application的DisplayMetrics
        appDisplayMetrics = application.getResources().getDisplayMetrics();
        //获取状态栏高度
        barHeight = getStatusBarHeight(application);
        Density.designWidth = designWidth;
        Density.designHeight = designHeight;

        if (appDensity == 0) {
            //初始化的时候赋值
            appDensity = appDisplayMetrics.density;
            appScaledDensity = appDisplayMetrics.scaledDensity;

            //添加字体变化的监听
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration newConfig) {
                    //字体改变后,将appScaledDensity重新赋值
                    if (newConfig != null && newConfig.fontScale > 0) {
                        appScaledDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {
                }
            });
        }
    }

    //此方法在BaseActivity中做初始化(如果不封装BaseActivity的话,直接用下面那个方法就好)
    public static void setDefault(Activity activity) {
        setAppOrientation(activity, Orientation.WIDTH);
    }

    //此方法用于在某一个Activity里面更改适配的方向
    public static void setOrientation(Activity activity, Orientation orientation) {
        setAppOrientation(activity, orientation);
    }

    /**
     * targetDensity
     * targetScaledDensity
     * targetDensityDpi
     * 这三个参数是统一修改过后的值
     * <p>
     * orientation:方向值,传入width或height
     */
    private static void setAppOrientation(@Nullable Activity activity, Orientation orientation) {

        if (activity == null || orientation == null) return;
        float targetDensity;

        if (orientation == Orientation.HEIGHT) {
            targetDensity = (appDisplayMetrics.heightPixels - barHeight) / designHeight;
        } else {
            targetDensity = appDisplayMetrics.widthPixels / designWidth;
        }

        Logger.w("屏幕宽度：",appDisplayMetrics.widthPixels,"px ，屏幕高度 : ",appDisplayMetrics.heightPixels+"px");

        Logger.w("dpiW:", appDisplayMetrics.widthPixels / designWidth
                , ",dpiH:", appDisplayMetrics.heightPixels / designHeight, ",targetDpi:", targetDensity);

        //设置 app 不随着系统字体的调整而变化,因为字体的大小也是根据density的，所以适配后保证字体大小不变，所以需要重新设置字体大小
        float targetScaledDensity = targetDensity * (appScaledDensity / targetDensity);
        Logger.w("字体 scaledDensity is :",targetScaledDensity);
        int targetDensityDpi = (int) (160 * targetDensity);
        DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
        activityDisplayMetrics.density = targetDensity;
        activityDisplayMetrics.scaledDensity = targetScaledDensity;
        activityDisplayMetrics.densityDpi = targetDensityDpi;
    }

    /**
     * 获取状态栏高度
     *
     * @param context context
     * @return 状态栏高度
     */
    private static int getStatusBarHeight(Context context) {
        // 获得状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    public enum Orientation {
        WIDTH, HEIGHT
    }
}