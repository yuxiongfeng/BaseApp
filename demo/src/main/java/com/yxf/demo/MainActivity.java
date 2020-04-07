package com.yxf.demo;

import com.wms.logger.Logger;
import com.yxf.baseapp.base.activity.BaseActivity;
import com.yxf.demo.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    @Override
    protected int inflateContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        Logger.w("now density is :",getResources().getDisplayMetrics().density);
    }

    @Override
    protected void initView() {
        super.initView();
        Logger.w("now MainActivity density is :",getResources().getDisplayMetrics().density);
        Logger.w("now MainActivity density is :",getResources().getDisplayMetrics().density);
    }

    @Override
    public int getStatusBarDrawable() {
        return R.drawable.drawable_status_bar;
    }
}
