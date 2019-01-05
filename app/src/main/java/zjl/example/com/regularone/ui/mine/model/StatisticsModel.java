package zjl.example.com.regularone.ui.mine.model;

import com.jaydenxiao.common.baserx.RxSchedulers;

import io.reactivex.Observable;
import zjl.example.com.regularone.api.Api;
import zjl.example.com.regularone.api.HostType;
import zjl.example.com.regularone.bean.StatisticsData;
import zjl.example.com.regularone.ui.mine.contract.StatisticsContract;

/**
 * Created by Administrator on 2018/11/12 0012.
 */

public class StatisticsModel implements StatisticsContract.Model {

    @Override
    public Observable<StatisticsData> getStatisticsData() {
        return Api.getDefault(HostType.PHOTO_HOST)
                .getStatistics("https://www.apiopen.top/findStatistics?appKey=00d91e8e0cca2b76f515926a36db68f5")
                .compose(RxSchedulers.io_main());
    }
}
