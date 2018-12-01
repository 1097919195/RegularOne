package zjl.example.com.regularone.ui.main.presenter;

import com.jaydenxiao.common.baserx.RxSubscriber2;

import zjl.example.com.regularone.bean.WeatherInfo;
import zjl.example.com.regularone.ui.main.contract.MainContract;

public class MainPresenter extends MainContract.Presenter {
    @Override
    public void getWeatherInfoRequest() {
        mRxManage.add(mModel.getWeatherInfo().subscribeWith(new RxSubscriber2<WeatherInfo>(mContext, false) {
            @Override
            protected void _onNext(WeatherInfo weatherInfo) {
                mView.returnWeatherInfo(weatherInfo);
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorTip(message);
            }
        }));
    }
}
