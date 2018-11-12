package zjl.example.com.regularone.ui.navigation.presenter;

import com.jaydenxiao.common.baserx.RxSubscriber2;

import java.util.List;

import zjl.example.com.regularone.bean.NavCategory;
import zjl.example.com.regularone.ui.navigation.contract.NavigationContract;

/**
 * Created by Administrator on 2018/11/12 0012.
 */

public class NavigationPresenter extends NavigationContract.Presenter {
    @Override
    public void getNavigationDataRequest() {
        mRxManage.add(mModel.getNavCategory().subscribeWith(new RxSubscriber2<List<NavCategory>>(mContext, false) {
            @Override
            protected void _onNext(List<NavCategory> navCategories) {
                mView.returnNavigationData(navCategories);

            }

            @Override
            protected void _onError(String message) {
                mView.showErrorTip(message);
            }
        }));
    }
}
