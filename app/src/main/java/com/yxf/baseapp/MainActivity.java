package com.yxf.baseapp;

import com.wms.logger.Logger;
import com.yxf.baseapp.base.activity.BaseActivity;
import com.yxf.baseapp.base.test.Obj;
import com.yxf.baseapp.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    @Override
    protected int inflateContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        final Obj obj = new Obj();
        Thread t1 = new Thread(new Runnable() {

            @Override
            public void run() {
                String val = null;
                for (int i = 0; i < 100; i++) {
                    val = "t1: " + i;
                    obj.addStr(String.valueOf(val));
                    System.out.println("add: " + val);

                    sleep(50);
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {

            @Override
            public void run() {
                String val = null;
                for (int i = 0; i < 100; i++) {
                    val = "t2: " + (100 + i);
                    obj.addStr(String.valueOf(val));
                    System.out.println("add: " + val);
                    sleep(50);
                }
            }
        });
        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
            String name = Thread.currentThread().getName();
            System.out.println("add Thread name: " + name + ", Obj size: " + obj.getSize());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

}
