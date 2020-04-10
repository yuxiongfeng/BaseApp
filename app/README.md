#### 项目根目录配置:
```
allprojects {
    repositories {
        google()
        jcenter()
        maven{
            url "https://dl.bintray.com/yxf22/coreMaven"
        }
    }
}
```

#### 添加依赖库:
```
 implementation 'com.yxf.androidx:baseApp:1.0.4'
 ```

#### 在application中初始化
```
  BaseEntrance.getInstance()
                .init(this)//初始化
                .registerActivityListener(this)//注册activity的生命周期
                .initRefresh()//初始化SmartRefreshLayout的默认Header和Footer
                .setDensity(480, 667);//设置设计图上的尺寸
```

#### 在activity中相关操作
```
   需要重写的方法：设置创建和状态栏相同高度view的颜色
   @Override
    public int getStatusBarDrawable() {
        return R.drawable.drawable_status_bar;
    }
```


#### 工具类介绍

>- CommonUtils

```
相关方法：
1.getMIMEType(String filePath); //获取文件夹的MIME类型（比如：判断是否是图片或者视频等）
2.getAppVersion(Context context); //获取app的版本号
3.getAppName(Context context);//获取app的名字
4.getDeviceImei(Context context);//Returns the unique device ID,return null if device id is not available.
5.isMobileNO(String mobile);//判断是否是手机号
6.listIsEmpty(List list);//判断list是否为空
7.openKeybord(EditText editText, Context mContext);//打开软键盘
8.closeKeybord(EditText editText, Context mContext);//关闭软键盘
9.
```

>- Density（用于屏幕适配）

```
相关方法：
1.首先在application初始化设计图的宽高
  BaseEntrance.getInstance().setDensity(480,667);

2.然后在baseActivity里面设置适配的方向
  Density.setOrientation(this, Density.Orientation.WIDTH);

```

>- DensityUtils

```
相关方法：
   ·dp2px() //dp转px
   ·px2dp() //px转dp
   ·sp2px() //sp转px
   ·px2sp() //px转sp
   ·getScreenSize() //获取屏幕大小（返回一个数组：第一项为宽，第二项为高）
   ·getScreenWidth() //获取屏幕宽度(px)
   ·getScreenHeight()//获取屏幕高度(px)
   ·getStatusHeight() //获取状态栏高度
   ·snapShotWithStatusBar() //获取当前屏幕截图（包括状态栏在内）
   ·snapShotWithoutStatusBar() //获取当前屏幕截图（不包括状态栏）
```

>- EventBusManager

```
使用前提条件：
 接收事件的activity需要重写的方法：
   ①isRegisterEventBus()
   ②
         @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
    }



内部方法：
   ·getInstance()
   ·register()
   ·unregister()
   ·post()



```

>- StatusBarUtil



>- BlackToast 自定义的toast

```
· show() //直接调用show方法即可。
```


>- ActivityManager 用于管理activity

```
前提条件：
 · BaseEntrance.getInstance().registerActivityListener(this)//需要在application里面先注册

 相关方法：
 · currentActivity()//获取当前activity
 · getRootActivity()
 · finishAllExceptRoot() //结束所有activity除了根activity
 · finishActivity(Activity activity) //删除指定activity
 · hasActivity(Class<?> clazz) //是否存在某个activity
 · finishAllActivity() //结束所有activity
 · appExit() //退出应用程序
 · keepActivity(Class<?>... cls) //只保留哪些activity，其余的删除
 · getTopActivity() //获取最顶部activity
 · getTopActivityName() //获取最顶部activity的名字
 · isAppBackground() //判断app是否处于后台
 · size() //获取任务栈中activity的数量

```


>- PathUtils 文件路径工具

```
相关方法：
 *   ·内部存储(internal)路径
 *    getRootPath() /system
 *    getDataPath() /data
 *    getDownloadCachePath() /cache
 *    getInternalAppDataPath() /data/data/packageName
 *    getInternalAppCodeCacheDir() /data/data/packageName/code_cache //自定义目录,用于缓存一些数据
 *    getInternalAppDbsPath() /data/data/packageName/databases  //自定义目录，主要用于保存一些数据库数据
 *    getInternalAppFilesPath() /data/data/packageName/files  //本地文件路径
 *    getInternalAppSpPath() /data/data/packageName/shared_prefs //本地sharedPrefs目录
 *
 *    ·外部存储(external)路径
 *     getExternalStoragePath()   /storage/emulated/0/   外部存储根目录
 *     getExternalMusicPath() /storage/emulated/0/Music
 *     getExternalAppDataPath() /storage/emulated/0/Android/data/packageName/
 *     getExternalAppCachePath() /storage/emulated/0/Android/data/packageName/cache
 *     getExternalAppFilesPath() /storage/emulated/0/Android/data/packageName/files
 *     isExternalStorageDisable() //判断外置sdk是否可用
 *
```

>- FileUtils

```
相关方法：
 *       ·getFileByPath() //通过路径获取文件，文件可能不存在
 *       ·isFileExists() //判断文件是否存在，可以通过file或者path
 *       ·rename() //文件重命名，通过文件或者文件名
 *       ·isDir() //判断是否是目录，通过文件或者文件名
 *       ·isFile() //判断是否是文件
 *       ·createOrExistsDir() //创建目录，可能目录已存在
 *       ·createOrExistsFile() //创建文件，如果文件存在创建不了
 *       ·createFileByDeleteOldFile() //创建文件，如果文件存在则先删除之前的文件再创建新的文件
 *       ·copyDir() 复制目录及目录下的所有文件
 *       ·copyFile() 复制文件
 *       ·deleteDir() 输出目录
 *       ·deleteFile() 删除文件
 *       ·listFilesInDir() //列出目录下的文件

```

>- FileIOUtils

```
相关方法：
 *      写入：
 *      · writeFileFromIS() 把inputStream 流 写入文件
 *      · writeFileFromBytesByStream() 把字符数组写入文件
 *      · writeFileFromBytesByChannel() 把字符数组通过FileChannel写入文件（效率高点）
 *      · writeFileFromBytesByMap() 把字符数组通过mapped写入文件（用在大文件写入时候效率高）
 *      · writeFileFromString() 把字符串写入文件
 *
 *      读取：
 *      ·readFile2List  把文件转为字符串数组（BufferedReader整行读取），每行内容为一个item
 *      ·readFile2String 把文件转为字符串
 *      ·readFile2BytesByStream 把文件转为字符数组，通过fileInputString
 *      ·readFile2BytesByChannel 把文件转为字符数组，通过fileChannel
 *      ·readFile2BytesByMap 把文件转为字符数据，通过mapped（读大文件）
 *
 *      ·setBufferSize() 设置字符数组buffer的大小
```

>- SPUtils (utils about shared preference)

```
相关方法：
  · getInstance() //获取SPUtils的实例对象，可以传入spName,如果不传入默认是"spUtils" ；
  也可传入操作模式，
  context.mode_private(私有模式，只能被本应用访问)，
  context.mode_append(检测文件是否存在，存在直接在后面追加，不存在新建文件) 主要是这两种模式。

  ·put() 存各种基本数据
  ·get() 取各种基本数据


```

