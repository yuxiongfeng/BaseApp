package com.yxf.baseapp.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.StringRes;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.wms.logger.Logger;
import com.yxf.baseapp.R;
import com.yxf.baseapp.base.component.BaseEntrance;
import com.yxf.baseapp.utils.ActivityManager;
import com.yxf.baseapp.utils.CommonUtils;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.pedant.SweetAlert.Type;

public class BaseViewModel extends ViewModel {
    /**
     * 数据加载状态
     */
    public ObservableField<Status> status = new ObservableField<>(Status.Loading);
    protected SweetAlertDialog mDialog;

    public BaseViewModel() {
    }

    /**
     * 隐藏dialog
     */
    protected void dismissDialog() {
        try {
            if (mDialog != null) {
                mDialog.dismiss();
                mDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示dialog
     */
    protected void showDialog(final String msg, final boolean canceable) {
        try {
            initDialog();
            mDialog.setTitleText(msg).changeAlertType(Type.PROGRESS_TYPE).setCancelable(canceable);
            mDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示dialog
     */
    protected void showDialog(@StringRes final int msg, final boolean canceable) {
        showDialog(CommonUtils.getString(msg), canceable);
    }

    /**
     * 显示dialog
     */
    protected void showDialog() {
        showDialog(R.string.string_loading, true);
    }

    /**
     * 显示dialog
     */
    protected void showDialog(final String msg) {
        showDialog(msg, true);
    }

    /**
     * 显示dialog
     */
    protected void showDialog(@StringRes final int msg) {
        showDialog(CommonUtils.getString(msg), true);
    }

    /**
     * 显示dialog
     */
    protected void showDialog(final String msg, final int type) {
        try {
            initDialog();
            mDialog.setTitleText(msg).changeAlertType(type);
            mDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示dialog
     */
    protected void showDialog(@StringRes final int msg, final int type) {
        showDialog(CommonUtils.getString(msg), type);
    }

    private void initDialog() {
        if (mDialog == null) {
            mDialog = new SweetAlertDialog(getContext());
        }
        mDialog.setOnDismissListener(null);
    }

    protected SweetAlertDialog getDialog() {
        initDialog();
        return mDialog;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDialog = null;
        Logger.d(getClass().getSimpleName() + " 销毁了");
    }

    /**
     * 跳转页面
     *
     * @param clz 所跳转的目的Activity类
     */
    protected void startActivity(Class<?> clz) {
        getContext().startActivity(new Intent(getContext(), clz));
    }

    /**
     * finish activity
     */
    public void finishActivity() {
        if (getContext() instanceof Activity) {
            ((Activity) getContext()).finish();
        }
    }

    /**
     * 获取资源字符串
     */
    public String getResString(int resId) {
        return BaseEntrance.getInstance().get().getResources().getString(resId);
    }

    protected Context getContext() {
        return ActivityManager.currentActivity();
    }

    /**
     * 加载数据的状态
     */
    public enum Status {
        Loading, Success, Fail, Empty, NO_NET
    }

}
