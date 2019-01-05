package zjl.example.com.regularone.ui.mine.contract;


import com.jaydenxiao.common.base.BaseModel;
import com.jaydenxiao.common.base.BasePresenter;
import com.jaydenxiao.common.base.BaseView;

import io.reactivex.Observable;
import zjl.example.com.regularone.bean.LogisticsData;
import zjl.example.com.regularone.bean.StatisticsData;


/**
 * des:图片列表contract
 * Created by xsf
 * on 2016.09.14:38
 */
public interface StatisticsContract {
    interface Model extends BaseModel {
        Observable<StatisticsData> getStatisticsData();
    }

    interface View extends BaseView {
        void returnStatisticsData(StatisticsData statisticsData);
    }
    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void getStatisticsDataRequest();
    }
}
