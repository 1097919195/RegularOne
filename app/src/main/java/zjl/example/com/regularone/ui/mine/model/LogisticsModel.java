package zjl.example.com.regularone.ui.mine.model;

import com.jaydenxiao.common.baserx.RxSchedulers;

import io.reactivex.Observable;
import zjl.example.com.regularone.api.Api;
import zjl.example.com.regularone.api.HostType;
import zjl.example.com.regularone.bean.LogisticsData;
import zjl.example.com.regularone.ui.mine.contract.LogisticsContract;

/**
 * Created by Administrator on 2018/11/12 0012.
 */

public class LogisticsModel implements LogisticsContract.Model {

    @Override
    public Observable<LogisticsData> getLogisticsData(String company, String express_num) {
        return Api.getDefault(HostType.PHOTO_HOST)
//                .getLogistics("http://www.kuaidi100.com/query?type=yunda&postid=3101775486667")
                .getLogistics("http://www.kuaidi100.com/query?type=" + company + "&postid=" + express_num)
                .compose(RxSchedulers.io_main());
    }
}
