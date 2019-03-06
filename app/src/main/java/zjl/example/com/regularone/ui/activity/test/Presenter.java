package zjl.example.com.regularone.ui.activity.test;

import com.jaydenxiao.common.baserx.RxSubscriber2;

/**
 * Created by asus-pc on 2019/2/21.
 */

public class Presenter extends Contract.Presenter{
    @Override
    public void getDataInfoRequest() {
        mRxManage.add(mModel.getDataInfo().subscribeWith(new RxSubscriber2<DataBean>(mContext, false) {
            @Override
            protected void _onNext(DataBean dataBean) {
                mView.returnDataInfo(dataBean);
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorTip(message);
            }
        }));
    }
}
