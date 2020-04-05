package com.yxf.baseapp.viewmodel;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends BaseViewModel {

    private List<String> titles = new ArrayList<>();

    public List<String> getTitles() {
        status.set(Status.Loading);
        if (titles != null) {
            titles.clear();
        }
        titles.add("test");
        titles.add("test");
        titles.add("test");
        titles.add("test");
        Thread t= new Thread(() -> {
            try {
                Thread.sleep(2000);

                if (titles.size()==0) {
                    status.set(Status.Empty);
                }else {
                    status.set(Status.Success);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t.start();
        return titles;
    }

}
