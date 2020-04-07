package com.yxf.baseapp;

import android.content.Context;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.wms.logger.Logger;
import com.yxf.baseapp.utils.CommonUtils;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.yxf.baseapp", appContext.getPackageName());
    }


    @Test
    public void testSynchronizedList() {
        final Obj obj = new Obj();
        Log.d("当前线程id :",Thread.currentThread().getId()+"");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 20; i++) {
                        obj.addStr("当前元素值：" + i);
//                        Log.d("添加", ":" + i);
                        Thread.sleep(500);

                        Log.d("run中的线程id :",Thread.currentThread().getId()+"");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


    @Test
    public void testEncode(){
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        String hello = CommonUtils.encodeString("hello");
        System.out.println("hello after encode is : "+hello);

//        String hello1 = CommonUtils.hexToString("hello");
//        System.out.println("hello covert hexString is :"+hello1);

        String tenToHex = Integer.toHexString(10);
        String s = Integer.toBinaryString(10);
        System.out.println("tenToHex: "+tenToHex+" , tenToBinary : "+s);


    }


}
