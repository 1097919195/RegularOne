package zjl.example.com.regularone.ui.main.contract;

import com.jaydenxiao.common.base.BaseModel;
import com.jaydenxiao.common.base.BasePresenter;
import com.jaydenxiao.common.base.BaseView;

import io.reactivex.Observable;
import zjl.example.com.regularone.bean.WeatherInfo;

public interface MainContract {
    interface Model extends BaseModel {
        Observable<WeatherInfo> getWeatherInfo();
    }

    interface View extends BaseView {
        void returnWeatherInfo(WeatherInfo weatherInfo);
    }
    abstract class Presenter extends BasePresenter<MainContract.View, MainContract.Model> {
        public abstract void getWeatherInfoRequest();
    }
}
