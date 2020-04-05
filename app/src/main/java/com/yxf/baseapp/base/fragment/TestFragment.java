package com.yxf.baseapp.base.fragment;


import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.Observable;
import androidx.lifecycle.ViewModelProviders;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wms.logger.Logger;
import com.yxf.baseapp.R;
import com.yxf.baseapp.databinding.FragmentTestLayoutBinding;
import com.yxf.baseapp.viewmodel.BaseViewModel;
import com.yxf.baseapp.viewmodel.MainViewModel;

public class TestFragment extends BaseViewModelFragment<FragmentTestLayoutBinding, MainViewModel> {
    @Override
    protected int inflateContentView() {
        return R.layout.fragment_test_layout;
    }

    @Override
    protected void fragmentInit() {

        viewmodel.status.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (viewmodel.status.get()!= BaseViewModel.Status.Loading) {
                    binding.idFresh.finishRefresh();
                }
            }
        });

        initRefreshLayout(binding.idFresh, new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                Logger.w("fragment开始刷新获取数据");
            }
        });
    }

    @Override
    protected MainViewModel getViewModel() {
        return ViewModelProviders.of(this).get(MainViewModel.class);
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected View getEmptyAndLoadingView() {
        return binding.idFresh;
    }
}
