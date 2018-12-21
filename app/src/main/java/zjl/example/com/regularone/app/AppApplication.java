package zjl.example.com.regularone.app;

import android.content.Context;

import com.danikula.videocache.HttpProxyCacheServer;
import com.jaydenxiao.common.baseapp.BaseApplication;
import com.jaydenxiao.common.commonutils.LogUtils;
import com.liulishuo.filedownloader.FileDownloader;
import com.polidea.rxandroidble2.RxBleClient;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.crashreport.CrashReport;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import java.util.LinkedHashMap;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;
import io.reactivex.plugins.RxJavaPlugins;
import zjl.example.com.regularone.BuildConfig;

/**
 * Created by Administrator on 2018/10/26 0026.
 */

public class AppApplication extends BaseApplication{
    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.logInit(BuildConfig.LOG_DEBUG);
        setRxJavaErrorHandler();

        ZXingLibrary.initDisplayOpinion(this);
        FileDownloader.setup(this);//注意作者已经不建议使用init方法

//        CrashReport.initCrashReport(getApplicationContext(),"68840e3481",true);
//        CrashReport.setUserId(this,"Riven,the Exile");
        Beta.autoCheckUpgrade = false;//取消初始化自动更新（在mainActivity中调用更新）
        Bugly.init(getApplicationContext(),"68840e3481",false);//统一处理异常上报和更新
        Bugly.setUserId(this,"Riven,the Exile");

        /**
         * 必须在 Application 的 onCreate 方法中执行 BGASwipeBackHelper.init 来初始化滑动返回
         * 第一个参数：应用程序上下文
         * 第二个参数：如果发现滑动返回后立即触摸界面时应用崩溃，请把该界面里比较特殊的 View 的 class 添加到该集合中，目前在库中已经添加了 WebView 和 SurfaceView
         */
        BGASwipeBackHelper.init(this, null);

    }

    /**
     * RxJava2 当取消订阅后(dispose())，RxJava抛出的异常后续无法接收(此时后台线程仍在跑，可能会抛出IO等异常),全部由RxJavaPlugin接收，需要提前设置ErrorHandler
     * 详情：http://engineering.rallyhealth.com/mobile/rxjava/reactive/2017/03/15/migrating-to-rxjava-2.html#Error Handling
     */
    private void setRxJavaErrorHandler() {
        RxJavaPlugins.setErrorHandler(throwable -> LogUtils.loge("throw test RxJava2===="+throwable.getMessage()));
    }


    private HttpProxyCacheServer proxy;

    public static HttpProxyCacheServer getProxy(Context context) {
        AppApplication app = (AppApplication) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer(this);
    }
}


