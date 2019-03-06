package zjl.example.com.regularone.ui.activity.test;

import com.jaydenxiao.common.base.BaseModel;
import com.jaydenxiao.common.base.BasePresenter;
import com.jaydenxiao.common.base.BaseView;

import io.reactivex.Observable;

/**
 * Created by asus-pc on 2019/2/21.
 */

public interface Contract {
    interface Model extends BaseModel {
        Observable<DataBean> getDataInfo();
    }
    interface View extends BaseView {
        void returnDataInfo(DataBean dataBean);
    }
    abstract class Presenter extends BasePresenter<Contract.View, Contract.Model> {
        public abstract void getDataInfoRequest();
    }
}
