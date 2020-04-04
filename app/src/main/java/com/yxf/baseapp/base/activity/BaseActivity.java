package com.yxf.baseapp.base.activity;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.wms.logger.Logger;
import com.yxf.baseapp.R;
import com.yxf.baseapp.utils.Density;
import com.yxf.baseapp.utils.DensityUtils;
import com.yxf.baseapp.utils.StatusBarUtil;
import com.yxf.baseapp.utils.Utils;

/**
 * @Description: 基类
 * @Author: yxf
 * @CreateDate: 2020/4/2 9:40
 * @UpdateUser: yxf
 * @UpdateDate: 2020/4/2 9:40
 */
public abstract class BaseActivity<DB extends ViewDataBinding> extends AppCompatActivity {
    protected DB binding;
    protected Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (isShowInBottom()) {
            overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
        }
        super.onCreate(savedInstanceState);
        mContext = this;
        //适配相关设置
        Density.setOrientation(this, getOrientation());
        Logger.w("density :",mContext.getResources().getDisplayMetrics().density);
        Utils.setStatusBarTextColor(this, isDarkIcon());
        long startTime = System.currentTimeMillis();
        int layoutId = inflateContentView();
        if (layoutId != 0) {
            binding = DataBindingUtil.setContentView(this, layoutId);
        }
        Logger.w("耗时:" + (System.currentTimeMillis() - startTime) + "," + this.getClass().getSimpleName());
        init();
        initView();
        initData();

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
        StatusBarUtil.setStatusBarDrawable(this, R.drawable.drawable_status_bar);
        initToolbar();
    }

    protected void setStatusBarColor() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.white));
    }

    protected Density.Orientation getOrientation() {
        return Density.Orientation.HEIGHT;
    }

    /**
     * 初始化toolbar
     */
    private void initToolbar() {

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
    }

    /**
     * 设置监听
     */
    protected void setListener() {

    }

}
