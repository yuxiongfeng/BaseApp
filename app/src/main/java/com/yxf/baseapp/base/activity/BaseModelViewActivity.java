package com.yxf.baseapp.base.activity;

import androidx.databinding.Observable;
import androidx.databinding.ViewDataBinding;

import com.yxf.baseapp.viewmodel.BaseViewModel;

public abstract class BaseModelViewActivity<DB extends ViewDataBinding, VM extends BaseViewModel> extends BaseActivity<DB> {
    protected VM viewModel;
    private Observable.OnPropertyChangedCallback mStatusCallback = new Observable.OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable sender, int propertyId) {
            runOnUiThread(() -> {
                switch (viewModel.status.get()) {
                    case Loading:
                        showLoading();
                        break;
                    case NO_NET:
                        setLoadTimeOut();
                        break;
                    case Success:
                        setLoadSuccess();
                        break;
                    case Fail:
                        setLoadError();
                        break;
                    case Empty:
                        setLoadEmpty();
                        break;
                }
            });

        }
    };

    @Override
    protected void init() {
        viewModel = getViewModel();
        addStatusListener();
    }

    private void addStatusListener() {
        if (viewModel != null) {
            viewModel.status.addOnPropertyChangedCallback(mStatusCallback);
        }
    }

    @Override
    protected void showLoading() {
        super.showLoading();
        //状态必须统一，不然出问题
        viewModel.status.set(BaseViewModel.Status.Loading);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (viewModel != null) {
            viewModel.status.removeOnPropertyChangedCallback(mStatusCallback);
        }
    }


    protected abstract VM getViewModel();
}
