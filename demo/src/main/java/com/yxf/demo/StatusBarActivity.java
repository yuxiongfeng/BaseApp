package com.yxf.demo;

import com.yxf.baseapp.base.activity.BaseActivity;
import com.yxf.baseapp.utils.StatusBarUtil;
import com.yxf.demo.databinding.ActivityStatusBarLayoutBinding;

/**
 * @Description:
 * @Author: yxf
 * @CreateDate: 2020/4/10 11:37
 * @UpdateUser: yxf
 * @UpdateDate: 2020/4/10 11:37
 */
public class StatusBarActivity extends BaseActivity<ActivityStatusBarLayoutBinding> {
    private boolean isBarVisibility = true;

    @Override
    protected int inflateContentView() {
        return R.layout.activity_status_bar_layout;
    }

    @Override
    protected void init() {
        getSupportFragmentManager().beginTransaction().add(R.id.id_container,new TestFragment()).commit();
    }
}
