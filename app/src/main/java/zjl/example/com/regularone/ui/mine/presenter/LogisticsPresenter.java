package zjl.example.com.regularone.ui.mine.presenter;

import com.jaydenxiao.common.baserx.RxSubscriber2;

import zjl.example.com.regularone.bean.LogisticsData;
import zjl.example.com.regularone.ui.mine.contract.LogisticsContract;

/**
 * Created by Administrator on 2018/11/12 0012.
 */

public class LogisticsPresenter extends LogisticsContract.Presenter {

    @Override
    public void getLogisticsDataRequest() {
        mRxManage.add(mModel.getLogisticsData().subscribeWith(new RxSubscriber2<LogisticsData>(mContext, true) {
            @Override
            protected void _onNext(LogisticsData logisticsData) {
                mView.returnLogisticsData(logisticsData);
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorTip(message);
            }
        }));
    }
}
