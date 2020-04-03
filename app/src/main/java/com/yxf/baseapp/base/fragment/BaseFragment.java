package com.yxf.baseapp.base.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

/**
 * @Description:
 * @Author: yxf
 * @CreateDate: 2020/4/2 9:40
 * @UpdateUser: yxf
 * @UpdateDate: 2020/4/2 9:40
 */
public abstract class BaseFragment<DB extends ViewDataBinding> extends Fragment {
    private Context mContext;
    protected DB binding;
    protected View mInflatedView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layoutId = inflateContentView();
        if (layoutId != 0) {
            binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), inflateContentView(), container, false);
        }
        if (binding == null) {
            getActivity().finish();
            return null;
        }
        mInflatedView = binding.getRoot();
        return mInflatedView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    protected abstract int inflateContentView();

}
