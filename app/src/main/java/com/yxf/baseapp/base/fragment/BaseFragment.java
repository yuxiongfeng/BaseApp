package com.yxf.baseapp.base.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wms.logger.Logger;
import com.yxf.baseapp.R;
import com.yxf.baseapp.base.bean.MessageEvent;
import com.yxf.baseapp.base.component.BaseEntrance;
import com.yxf.baseapp.utils.ActivityManager;
import com.yxf.baseapp.utils.BlackToast;
import com.yxf.baseapp.utils.CommonUtils;
import com.yxf.baseapp.utils.EventBusManager;
import com.yxf.baseapp.view.LoadingLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.pedant.SweetAlert.SweetMessageDialog;
import cn.pedant.SweetAlert.Type;

/**
 * @Description:
 * @Author: yxf
 * @CreateDate: 2020/4/2 9:40
 * @UpdateUser: yxf
 * @UpdateDate: 2020/4/2 9:40
 */
public abstract class BaseFragment<DB extends ViewDataBinding> extends Fragment {
    private Context mContext;
    protected DB binding;
    protected View mInflatedView;
    protected LoadingLayout mLoadingLayout;
    protected SweetMessageDialog mDialog;

    //延迟加载数据
    protected boolean isVisible = false;//当前Fragment是否可见
    private boolean isInitView = false;//是否与View建立起映射关系
    private boolean isFirstLoad = true;//是否是第一次加载数据

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            long startTime = System.currentTimeMillis();
            binding = DataBindingUtil.inflate(getLayoutInflater(), inflateContentView(), container, false);
            if (binding == null) {
                getActivity().finish();
                return null;
            }
            mInflatedView = binding.getRoot();
            TextView titleText = mInflatedView.findViewById(R.id.title);
            if (titleText != null && !TextUtils.isEmpty(getTopCenterText())) {
                titleText.setText(getTopCenterText());
            }
            fragmentInit();
            initView();
            initEmptyView();
            if (isLazyLoad()) {
                lazyLoadData();
            } else {
                initData();
            }
            if (isRegisterEventBus()) {
                EventBusManager.getInstance().register(this);
            }
            Logger.w("耗时:" + (System.currentTimeMillis() - startTime) + "," + this.getClass().getSimpleName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mInflatedView;
    }

    /**
     * 为fragment设置布局
     *
     * @return layout布局
     */
    abstract protected int inflateContentView();

    /**
     * 初始化fragment
     */
    abstract protected void fragmentInit();


    protected void initView() {
    }


    /**
     * 初始化数据
     */
    protected void initData() {
        if (getEmptyAndLoadingView() != null && mLoadingLayout != null) {
            showLoading();
        }
    }

    /**
     * 标题
     */
    public String getTopCenterText() {
        return "";
    }

    /**
     * 初始化下拉刷新
     */
    protected void initRefreshLayout(SmartRefreshLayout refreshLayout, OnRefreshListener onRefreshListener) {
        if (refreshLayout == null) return;
        initRefreshLayout(refreshLayout, onRefreshListener, null);
    }

    /**
     * 初始化下拉刷新
     */
    protected void initRefreshLayout(SmartRefreshLayout refreshLayout, OnRefreshListener onRefreshListener, OnLoadMoreListener onLoadMoreListener) {
        if (refreshLayout == null) return;
        refreshLayout.setEnableScrollContentWhenLoaded(true);
        if (onRefreshListener != null) {
            refreshLayout.setEnableRefresh(true);
            refreshLayout.setOnRefreshListener(onRefreshListener);
        }

        if (onLoadMoreListener != null) {
            refreshLayout.setOnLoadMoreListener(onLoadMoreListener);
            refreshLayout.setEnableLoadMore(true);
        } else {
            refreshLayout.setEnableLoadMore(false);
        }
    }

    /**
     * 初始化没有数据时候的view
     */
    private void initEmptyView() {
        if (getEmptyAndLoadingView() == null) return;

        mLoadingLayout = LoadingLayout.wrap(getEmptyAndLoadingView());
        if (generateEmptyLayout() != 0) {
            mLoadingLayout.setEmpty(generateEmptyLayout());
            if (!TextUtils.isEmpty(getEmptyText())) {
                mLoadingLayout.setEmptyText(getEmptyText());
            }
            mLoadingLayout.setOnEmptyInflateListener(new LoadingLayout.OnInflateListener() {
                @Override
                public void onInflate(View inflated) {
                    initEmptyInflateListener(inflated);
                }
            });
        }

        if (generateLoadingLayout() != 0) {
            mLoadingLayout.setLoading(generateLoadingLayout());
        }

        if (BaseEntrance.getInstance().isLogin()) {
            if (generateTimeoutLayout() != 0) {
                mLoadingLayout.setError(generateTimeoutLayout());
            } else {
                mLoadingLayout.setErrorImage(R.drawable.no_net_bitmap);
            }
        } else {
            mLoadingLayout.setError(generateNotLoginLayout());
            initNotLoginInflateListener();
        }

        mLoadingLayout.setRetryListener(v -> {
            mLoadingLayout.showLoading();
            mLoadingLayout.postDelayed(this::initData, 300);
        });
    }


    /**
     * 是否懒加载
     */
    protected boolean isLazyLoad() {
        return false;
    }

    /**
     * 未登录布局监听器
     */
    private void initNotLoginInflateListener() {
        if (mLoadingLayout == null) return;
        mLoadingLayout.setOnErrorInflateListener(inflated -> {
            TextView goToLogin = inflated.findViewById(R.id.id_go_to_login);
            if (goToLogin != null) {
                goToLogin.setText(Html.fromHtml("赶快<font color='#30b8ff'><U>去登录</U></font>吧"));
                goToLogin.setOnClickListener(v -> {
                    BlackToast.show("点击了登录按钮，请在这里处理登录逻辑。。。");
                });
            }
            TextView notLoginTips = inflated.findViewById(R.id.id_not_login_tips);
            if (getNotLoginTips() != 0) {
                notLoginTips.setText(getNotLoginTips());
            }
        });
    }

    /**
     * 未登录提示
     */
    protected @StringRes
    int getNotLoginTips() {
        return 0;
    }

    /**
     * 获取加载中，加载失败，空白页的显示的view
     */
    protected View getEmptyAndLoadingView() {
        return null;
    }

    /**
     * 设置加载失败
     */
    protected void setLoadError() {
        if (mLoadingLayout == null) return;
        mLoadingLayout.showError();
    }

    /**
     * 设置加载成功
     */
    protected void setLoadSuccess() {
        if (mLoadingLayout == null) return;
        mLoadingLayout.showContent();
    }

    /**
     * 设置网络异常
     */
    protected void setLoadTimeOut() {
        if (mLoadingLayout == null) return;
        mLoadingLayout.showError();
    }

    /**
     * 设置加载数据为空
     */
    protected void setLoadEmpty() {
        if (mLoadingLayout == null) return;
        if (generateEmptyLayout() == 0) {
            mLoadingLayout.showContent();
        } else {
            mLoadingLayout.showEmpty();
        }
    }

    /**
     * 数据为空的时候显示的文字
     */
    protected String getEmptyText() {
        return "";
    }

    /**
     * empty view监听器
     */
    protected void initEmptyInflateListener(View emptyView) {
        TextView retryBtn = emptyView.findViewById(R.id.retry_button);
        if (!TextUtils.isEmpty(getRetryText())) {
            retryBtn.setText(getRetryText());
        }
        if (retryBtn != null) {
            retryBtn.setOnClickListener(v -> {
                doRetry();
            });
        }
    }

    /**
     * 重试按钮文字
     */
    protected String getRetryText() {
        return "";
    }

    protected void doRetry() {
        initData();
    }


    /**
     * 设置正在加载数据
     */
    protected void showLoading() {
        if (mLoadingLayout == null) return;
        mLoadingLayout.showLoading();
    }

    /**
     * 设置加载中页面布局
     */
    protected @LayoutRes
    int generateLoadingLayout() {
        return R.layout.layout_loading;
    }

    /**
     * 设置重试页面布局
     */
    protected @LayoutRes
    int generateTimeoutLayout() {
        return R.layout.layout_timeout;
    }

    /**
     * 设置空页面布局
     */
    protected @LayoutRes
    int generateEmptyLayout() {
        return R.layout.layout_empty;
    }

    /**
     * 设置未登录布局
     */
    protected @LayoutRes
    int generateNotLoginLayout() {
        return R.layout.layout_not_login;
    }


    /**
     * 设置监听
     */
    protected void setListener() {
    }

    /**
     * 是否显示返回键
     *
     * @return
     */
    private boolean showBackBtn() {
        return false;
    }

    protected int getBackIcon() {
        return R.drawable.btn_back_img;
    }

    /**
     * 根据字符串资源id返回字符串
     */
    protected String getResString(int id) {
        return this.getResources().getString(id);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Logger.w("内存不足:" + getClass().getSimpleName());
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    protected SweetMessageDialog getDialog() {

        Activity activity = ActivityManager.currentActivity();

        boolean added = isAdded();

        Logger.w("getActivity():" + this.getClass().getSimpleName());

        if (mDialog == null) {
            mDialog = new SweetMessageDialog(getActivity());
            mDialog.changeAlertType(Type.PROGRESS_TYPE).setTitleText(getString(R.string.string_loading));
            mDialog.setCancelable(false);
            mDialog.setOnCancelListener(dialog -> onDialogCancel());
            mDialog.setOnDismissListener(dialog -> onDialogDismiss());
        }
        return mDialog;
    }

    /**
     * 隐藏dialog
     */
    public void dismissDialog() {
        runOnUiThread(() -> getDialog().dismiss());
    }

    protected void onDialogDismiss() {
    }

    protected void onDialogCancel() {
    }


    /**
     * 显示dialog
     */
    public void showDialog() {
        showDialog(R.string.string_loading, true);
    }

    /**
     * 显示dialog
     */
    public void showDialog(boolean cancelable) {
        showDialog("", cancelable);
    }

    public void showDialog(int title) {
        showDialog(CommonUtils.getString(title), true);
    }

    /**
     * 显示dialog
     */
    public void showDialog(final String msg, final boolean canceable) {
        runOnUiThread(() -> {
            getDialog().setCancelable(canceable);
            if (!getDialog().isShowing()) {
                getDialog().changeAlertType(Type.PROGRESS_TYPE)
                        .setTitleText(msg);
                getDialog().show();
            }
        });
    }

    /**
     * 显示dialog
     */
    public void showDialog(final int msg, final boolean canceable) {
        showDialog(getString(msg), canceable);
    }

    /**
     * 显示dialog
     */
    public void showDialog(final String msg, final int type) {
        runOnUiThread(() -> {
            if (!getDialog().isShowing()) {
                getDialog().changeAlertType(type)
                        .setTitleText(msg);
                getDialog().show();
            }
        });
    }

    /**
     * 返回
     */
    protected void finish() {
        getActivity().onBackPressed();
    }

    /**
     * 在主线程运行
     *
     * @param runnable runnable
     */
    protected void runOnUiThread(Runnable runnable) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(runnable);
        }
    }

    /**
     * 是否注册eventbus
     */
    protected boolean isRegisterEventBus() {
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isVisible = true;
            lazyLoadData();
        } else {
            isVisible = false;
        }
    }

    @Override
    public void onDestroy() {
        if (isRegisterEventBus()) {
            EventBusManager.getInstance().unregister(this);
        }
        Logger.w("onDestroy:" + this.getClass().getSimpleName());
        super.onDestroy();
    }

    private void lazyLoadData() {
        if (isFirstLoad) {
            Logger.w("第一次加载 " + " isInitView  " + isInitView + "  isVisible  " + isVisible + "   " + this.getClass().getSimpleName());
        } else {
            Logger.w("不是第一次加载" + " isInitView  " + isInitView + "  isVisible  " + isVisible + "   " + this.getClass().getSimpleName());
        }
        if (!isFirstLoad || !isVisible || !isInitView) {
            Logger.w("不加载" + "   " + this.getClass().getSimpleName());
            return;
        }
        Logger.w("完成数据第一次加载");
        initData();
        isFirstLoad = false;
    }


}
