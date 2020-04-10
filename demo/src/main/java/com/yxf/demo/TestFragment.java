package com.yxf.demo;

import com.yxf.baseapp.base.fragment.BaseFragment;
import com.yxf.demo.databinding.FragmentTestLayoutBinding;

/**
 * @Description:
 * @Author: yxf
 * @CreateDate: 2020/4/10 14:35
 * @UpdateUser: yxf
 * @UpdateDate: 2020/4/10 14:35
 */
public class TestFragment  extends BaseFragment<FragmentTestLayoutBinding> {
    @Override
    protected int inflateContentView() {
        return R.layout.fragment_test_layout;
    }

    @Override
    protected void fragmentInit() {

    }
}
