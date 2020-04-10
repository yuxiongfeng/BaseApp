package com.yxf.demo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Environment;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.wms.logger.Logger;
import com.yxf.baseapp.base.activity.BaseActivity;
import com.yxf.baseapp.constant.TimeConstants;
import com.yxf.baseapp.utils.CommonUtils;
import com.yxf.baseapp.utils.FileIOUtils;
import com.yxf.baseapp.utils.FileUtils;
import com.yxf.baseapp.utils.PathUtils;
import com.yxf.baseapp.utils.TimeUtils;
import com.yxf.baseapp.viewmodel.BaseViewModel;
import com.yxf.demo.databinding.ActivityMainBinding;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.List;

import io.reactivex.functions.Consumer;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    public static final String TAG = "file operator : ";

    final RxPermissions rxPermissions = new RxPermissions(this);
    public static final String LOG_PATH = PathUtils.getExternalAppFilesPath() + "/test_log/test.txt";

    @Override
    protected int inflateContentView() {
        return R.layout.activity_main;
    }

    @SuppressLint("CheckResult")
    @Override
    protected void init() {
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (!aBoolean) {
                            finish();
                        }
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void initView() {
        super.initView();

        Logger.w("now MainActivity density is :", getResources().getDisplayMetrics().density);
        File[] files;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            files = getExternalFilesDirs(null);
            for (File file : files) {
                Logger.w(TAG, "内部存储文件列表 ： ", file.getAbsolutePath());
            }
        }
        //获取外部存储根目录
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        Logger.w(TAG, "externalStorageDirectory:,", externalStorageDirectory.getAbsolutePath());
        File fileTest = new File(externalStorageDirectory, "测试创建外部存储文件夹");
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {//如果已经挂载
            //sd卡已经挂载，可以进行读写操作了
            Logger.w(TAG, "文件已挂载");
        } else {
            //sd未挂载，在此进行提示
            Logger.w(TAG, "文件未挂载");
        }
        if (!fileTest.exists()) {
            boolean isSuccess = fileTest.mkdirs();
            Logger.w(TAG, "创建：", isSuccess ? "成功" : "失败");
        }

        //外部存储包名下的文件测试
        File test = mContext.getExternalFilesDir("test");//创建并返回
        Logger.w(TAG, "test 文件路径 ： ", test.getAbsolutePath());


        //获取内部存储路径
        File dataDirectory = Environment.getDataDirectory();
        Logger.w(TAG, "内部存储根路径 ：", dataDirectory.getAbsolutePath());
        String absolutePath = getFilesDir().getAbsolutePath();
        Logger.w(TAG, "内部存储中的files路径 : ", absolutePath);
        File cacheDir = getCacheDir();
        Logger.w(TAG, "内部存储缓存文件路径： ", cacheDir.getAbsolutePath());


        String internalCachePath = PathUtils.getInternalAppCachePath() + "/database/";
        if (!FileUtils.isFileExists(internalCachePath)) {
            Logger.w("文件不存在");
            boolean orExistsFile1 = FileUtils.createOrExistsFile(internalCachePath);
            Logger.w(orExistsFile1 ? "创建cache 下的dbs目录成功" : "创建dbs目录失败");
        } else {
            Logger.w("目录已存在");
        }

        String testPath = PathUtils.getInternalAppCodeCacheDir() + "/test/test.txt";
        if (!FileUtils.isFileExists(testPath)) {
            FileUtils.createOrExistsFile(testPath);
        }

        String testDPath = PathUtils.getInternalAppDbsPath() + "/test/test.txt";
        if (!FileUtils.isFileExists(testDPath)) {
            FileUtils.createOrExistsFile(testDPath);
        }


        //mkdirs
        String testDir = PathUtils.getInternalAppDataPath() + "/test/";
        if (!FileUtils.isFileExists(testDir)) {
            FileUtils.createOrExistsDir(testDir);
        }

        @SuppressLint("WrongConstant")
        SharedPreferences sharedPreferences = getSharedPreferences("test_sp", Context.MODE_APPEND);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("test", "it is my test data");
        edit.apply();//高效，没有返回，先写入内存，最后写入磁盘

        File internalFile = new File(cacheDir, "test/hello.txt");
        if (!internalFile.exists()) {
            boolean mkdirs = internalFile.mkdirs();
            if (mkdirs) {
                Logger.w(TAG, "内部存储创建文件成功");
            } else {
                Logger.w(TAG, "内部存储创建文件失败");
            }
        }
        OutputStream inputStream = null;
        String testStr = "hello world";
        try {
            inputStream = new FileOutputStream(internalFile);
            inputStream.write(testStr.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                inputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        boolean orExistsFile = FileUtils.createOrExistsDir(PathUtils.getExternalStoragePath() + "/yxftest");
        boolean createFileStatus = FileUtils.createOrExistsFile(PathUtils.getExternalAppFilesPath() + "/test_log/test.txt");
        FileUtils.createOrExistsFile(PathUtils.getExternalAppFilesPath() + "/test_log/test2.txt");
        FileUtils.createOrExistsFile(PathUtils.getExternalAppFilesPath() + "/test_log/test3.txt");
        FileUtils.createOrExistsFile(PathUtils.getExternalAppFilesPath() + "/test_log/test4.txt");

        String testLog = "it is my test logger ";
        FileIOUtils.writeFileFromBytesByStream(LOG_PATH, testLog.getBytes());
        boolean copy = FileUtils.copyDir(PathUtils.getExternalAppFilesPath() + "/test_log/", PathUtils.getExternalStoragePath() + "/ttt/");
        Logger.w(createFileStatus ? "创建成功" : "创建失败");
        Logger.w(copy ? "copy成功" : "copy失败");

        boolean copyFile = FileUtils.copyFile(PathUtils.getExternalAppFilesPath() + "/test_log/test2.txt", PathUtils.getExternalStoragePath() + "/ttt/newtest.txt");
        Logger.w(copyFile ? "copyFile成功" : "copyFile失败");

        List<File> files1 = FileUtils.listFilesInDir(PathUtils.getExternalStoragePath() + "/ttt");


        long lastModified = FileUtils.getFileLastModified(PathUtils.getExternalAppFilesPath() + "/test_log/test2.txt");
        Logger.w("last modified time is :", lastModified);

        String last = TimeUtils.millis2String(System.currentTimeMillis() - 100000000l);
        String start = TimeUtils.millis2String(System.currentTimeMillis() + 10000000l);
        String current = TimeUtils.millis2String(System.currentTimeMillis());

        String fitTimeSpan = TimeUtils.getFitTimeSpan(start, current, 5);
        Logger.w("fit time span :", fitTimeSpan);

        String fitTimeSpanByNow = TimeUtils.getFitTimeSpanByNow(last, 5);
        Logger.w("fitTimeSpanByNow :", fitTimeSpanByNow);


        String friendlyTimeSpanByNow = TimeUtils.getFriendlyTimeSpanByNow(start);
        Logger.w("friendlyTimeSpanByNow :", friendlyTimeSpanByNow);

        String chineseWeek = TimeUtils.getChineseWeek(System.currentTimeMillis());
        Logger.w("chineseWeek :", chineseWeek);

        String usWeek = TimeUtils.getUSWeek(System.currentTimeMillis());
        Logger.w("usWeek :", usWeek);

        int valueByCalendarField = TimeUtils.getValueByCalendarField(System.currentTimeMillis(), Calendar.YEAR);
        Logger.w("valueByCalendarField :", valueByCalendarField);

        binding.idHello.setOnClickListener(v -> startActivity(new Intent(this, StatusBarActivity.class)));
    }

    @Override
    public int getStatusBarDrawable() {
        return R.drawable.drawable_status_bar;
    }
}
