package com.yxf.baseapp;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: yxf
 * @CreateDate: 2020/4/3 19:04
 * @UpdateUser: yxf
 * @UpdateDate: 2020/4/3 19:04
 */
public class Obj {
    private List<String> list = new ArrayList<>();

    public void addStr(String str) {
        Log.d("添加", ":" + str);
        list.add(str);
    }

    public void remove(String str) {
        if (list.contains(str)) {
            list.remove(str);
        }
    }

    public int getSize() {
        return null == list ? 0 : list.size();
    }
}
