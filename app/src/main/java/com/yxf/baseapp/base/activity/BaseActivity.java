package com.yxf.baseapp.base.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wms.logger.Logger;
import com.yxf.baseapp.R;
import com.yxf.baseapp.base.bean.MessageEvent;
import com.yxf.baseapp.base.component.BaseEntrance;
import com.yxf.baseapp.utils.BlackToast;
import com.yxf.baseapp.utils.Density;
import com.yxf.baseapp.utils.EventBusManager;
import com.yxf.baseapp.utils.StatusBarUtil;
import com.yxf.baseapp.utils.Utils;
import com.yxf.baseapp.view.LoadingLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.pedant.SweetAlert.Type;

/**
 * @Description: activity基类
 * @Author: yxf
 * @CreateDate: 2020/4/2 9:40
 * @UpdateUser: yxf
 * @UpdateDate: 2020/4/2 9:40
 */
public abstract class BaseActivity<DB extends ViewDataBinding> extends AppCompatActivity {
    protected DB binding;
    protected Context mContext;
    protected LoadingLayout mLoadingLayout;
    protected SweetAlertDialog mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (isShowInBottom()) {
            overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
        }
        super.onCreate(savedInstanceState);
        //适配相关设置
        Density.setOrientation(this, getOrientation());
        int layoutId = inflateContentView();
        if (layoutId != 0) {
            binding = DataBindingUtil.setContentView(this, layoutId);
        }
        mContext = this;
        Utils.setStatusBarTextColor(this, isDarkIcon());
        long startTime = System.currentTimeMillis();
        Logger.w("耗时:" + (System.currentTimeMillis() - startTime) + "," + this.getClass().getSimpleName());
        init();
        setStatusBar();
        initView();
        setListener();
        initEmptyView();
        initData();
        setTopTextView();
        if (isRegisterEventBus()) {
            EventBusManager.getInstance().register(this);
        }
    }

    private boolean isDarkIcon() {
        return false;
    }

    /**
     * activity是否从底部弹出
     *
     * @return
     */
    private boolean isShowInBottom() {
        return false;
    }

    protected void setStatusBar() {
        StatusBarUtil.setStatusBarDrawable(this, getStatusBarDrawable());
        initToolbar();
    }

    /**
     * 创建和状态栏高度一样的半透明矩形
     *
     * @return
     */
    public int getStatusBarDrawable() {
        return R.drawable.drawable_status_bar;
    }

    protected void setStatusBarColor() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.white));
    }

    protected Density.Orientation getOrientation() {
        return Density.Orientation.WIDTH;
    }

    /**
     * 初始化toolbar
     */
    private void initToolbar() {
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setStatusBarColor();
        if (null != findViewById(R.id.toolbar)) {
            Toolbar toolbar = findViewById(R.id.toolbar);
            toolbar.setTitle("");
            if (getBackIcon() != -1) {
                toolbar.setNavigationIcon(getBackIcon());
            }
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(showBackBtn());
            getSupportActionBar().setHomeButtonEnabled(true);
            toolbar.setNavigationOnClickListener(v -> onBackPressed());
        }
    }

    /**
     * 为activity设置布局
     *
     * @return layout布局
     */
    abstract protected int inflateContentView();

    /**
     * 初始化数据，在initView()之前调用
     */
    abstract protected void init();

    /**
     * 初始化布局
     */
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

    protected void setTopTextView() {
        TextView titleText = findViewById(R.id.title);
        if (titleText != null && !TextUtils.isEmpty(getTopCenterText())) {
            titleText.setText(getTopCenterText());
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onActionBackClick();
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onActionBackClick() {
        finish();
    }

    /**
     * 根据字符串资源id返回字符串
     */
    protected String getResString(int id) {
        return this.getResources().getString(id);
    }

    /**
     * 获取color资源对应的颜色int值
     */
    protected int getResColor(int id) {
        return ContextCompat.getColor(this, id);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Logger.w("内存不足:" + getClass().getSimpleName());
    }

    /**
     * 是否注册eventBus
     */
    protected boolean isRegisterEventBus() {
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
    }

    protected void initDialog() {
        mDialog = new SweetAlertDialog(this, Type.PROGRESS_TYPE);
        mDialog.setTitleText(getString(R.string.string_loading));
        mDialog.setCancelable(true);
        mDialog.setOnDismissListener(dialog -> onDialogDismiss());
    }

    protected void onDialogDismiss() {

    }

    /**
     * 隐藏dialog
     */
    public void dismissDialog() {
        runOnUiThread(() -> {
            if (mDialog == null) {
                initDialog();
            }
            mDialog.dismiss();
        });
    }

    /**
     * 显示dialog
     */
    public void showDialog() {
        if (mDialog == null) {
            initDialog();
        }
        showDialog(R.string.string_loading, true);
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
    public void showDialog(String msg) {
        if (mDialog == null) {
            initDialog();
        }
        showDialog(msg, true);
    }

    /**
     * 显示dialog
     */
    public void showDialog(boolean cancelable) {
        if (mDialog == null) {
            initDialog();
        }
        showDialog("", cancelable);
    }

    /**
     * 显示dialog
     */
    public void showDialog(final String msg, final boolean canceable) {
        runOnUiThread(() -> {
            if (mDialog == null) {
                initDialog();
            }
            if (!mDialog.isShowing()) {
                mDialog.changeAlertType(Type.PROGRESS_TYPE)
                        .setTitleText(msg);
                mDialog.show();
            }
        });
    }

    /**
     * 显示dialog
     */
    public void showDialog(final String msg, final int type) {
        runOnUiThread(() -> {
            if (mDialog == null) {
                initDialog();
            }
            if (!mDialog.isShowing()) {
                mDialog.changeAlertType(type)
                        .setTitleText(msg);
                mDialog.show();
            }
        });
    }

}
