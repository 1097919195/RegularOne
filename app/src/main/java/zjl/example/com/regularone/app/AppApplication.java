package zjl.example.com.regularone.app;

import com.jaydenxiao.common.baseapp.BaseApplication;
import com.jaydenxiao.common.commonutils.LogUtils;
import com.polidea.rxandroidble2.RxBleClient;

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
    }

    /**
     * RxJava2 当取消订阅后(dispose())，RxJava抛出的异常后续无法接收(此时后台线程仍在跑，可能会抛出IO等异常),全部由RxJavaPlugin接收，需要提前设置ErrorHandler
     * 详情：http://engineering.rallyhealth.com/mobile/rxjava/reactive/2017/03/15/migrating-to-rxjava-2.html#Error Handling
     */
    private void setRxJavaErrorHandler() {
        RxJavaPlugins.setErrorHandler(throwable -> LogUtils.loge("throw test RxJava2===="+throwable.getMessage()));
    }
}
