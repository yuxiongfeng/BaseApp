package com.yxf.baseapp;

import com.yxf.baseapp.base.activity.BaseActivity;
import com.yxf.baseapp.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    @Override
    protected int inflateContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {

    }

}
