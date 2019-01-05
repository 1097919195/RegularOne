package zjl.example.com.regularone.ui.mine.presenter;

import com.jaydenxiao.common.baserx.RxSubscriber2;

import zjl.example.com.regularone.bean.StatisticsData;
import zjl.example.com.regularone.ui.mine.contract.StatisticsContract;

/**
 * Created by Administrator on 2018/11/12 0012.
 */

public class StatisticsPresenter extends StatisticsContract.Presenter {

    @Override
    public void getStatisticsDataRequest() {
        mRxManage.add(mModel.getStatisticsData().subscribeWith(new RxSubscriber2<StatisticsData>(mContext, true) {
            @Override
            protected void _onNext(StatisticsData statisticsData) {
                mView.returnStatisticsData(statisticsData);
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorTip(message);
            }
        }));
    }
}
