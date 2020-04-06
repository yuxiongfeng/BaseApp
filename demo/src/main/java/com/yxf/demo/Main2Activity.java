package com.yxf.demo;


import com.wms.logger.Logger;
import com.yxf.baseapp.base.activity.BaseActivity;
import com.yxf.demo.databinding.ActivityMain2Binding;

public class Main2Activity extends BaseActivity<ActivityMain2Binding> {

    @Override
    protected int inflateContentView() {
        return R.layout.activity_main2;
    }
    @Override
    protected void init() {
        Logger.w("now Main2Activity density is :",getResources().getDisplayMetrics().density);
    }
}
