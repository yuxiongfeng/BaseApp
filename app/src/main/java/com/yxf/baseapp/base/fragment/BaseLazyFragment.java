package com.yxf.baseapp.base.fragment;


import androidx.databinding.ViewDataBinding;

/**
 * Fragment的基类
 */
public abstract class BaseLazyFragment<DB extends ViewDataBinding> extends BaseFragment<DB> {
    @Override
    protected boolean isLazyLoad() {
        return true;
    }
}
