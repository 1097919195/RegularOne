package zjl.example.com.regularone.ui.main.module;

import com.jaydenxiao.common.baserx.RxSchedulers;

import io.reactivex.Observable;
import zjl.example.com.regularone.api.Api;
import zjl.example.com.regularone.api.HostType;
import zjl.example.com.regularone.bean.WeatherInfo;
import zjl.example.com.regularone.ui.main.contract.MainContract;

public class MainModule implements MainContract.Model {
    @Override
    public Observable<WeatherInfo> getWeatherInfo() {
        return Api.getDefault(HostType.WEATHER_HOST)
                .getWeather("w3ghwrnxngne3zfa","hangzhou")
                .compose(RxSchedulers.io_main());
    }
}
