package com.yxf.baseapp.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;

import com.yxf.baseapp.base.component.BaseEntrance;

public class DensityUtils {

    /**
     * convert dp to px
     * @param dp
     * @return
     */
    public static int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, BaseEntrance.getInstance().get().getResources().getDisplayMetrics());
    }

    /**
     * convert sp to px
     * @param sp
     * @return
     */
    public static int sp2px(float sp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,sp,BaseEntrance.getInstance().get().getResources().getDisplayMetrics());
    }

    /**
     * convert px to dp
     *
     * @param context context
     * @param pxValue px value
     * @return px value
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * convert px to sp
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * get Phone screen's width and height
     *
     * @param context context
     * @return contain width and height's array
     */
    public static int[] getScreenSize(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int[] arr = new int[2];
        arr[0] = outMetrics.widthPixels;
        arr[1] = outMetrics.heightPixels;
        return arr;
    }

    /**
     * get the width of screen
     *
     * @param context context
     * @return the width
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * get the height of screen
     *
     * @param context context
     * @return the height of screen
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * get status's height
     *
     * @param context context
     * @return status's height
     */
    public static int getStatusHeight(Context context) {

        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    /**
     * Gets the current screen capture, including the status bar
     *
     * @param activity the current activity
     * @return the bitmap of snapshot
     */
    public static Bitmap snapShotWithStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
        view.destroyDrawingCache();
        return bp;

    }

    /**
     * Gets the current screen capture, excluding the status bar
     *
     * @param activity the current activity
     * @return the bitmap of snapshot
     */
    public static Bitmap snapShotWithoutStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height
                - statusBarHeight);
        view.destroyDrawingCache();
        return bp;
    }
}
