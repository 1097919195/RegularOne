package zjl.example.com.regularone.ui.navigation.contract;


import com.jaydenxiao.common.base.BaseModel;
import com.jaydenxiao.common.base.BasePresenter;
import com.jaydenxiao.common.base.BaseView;

import java.util.List;

import io.reactivex.Observable;
import zjl.example.com.regularone.bean.HttpResponse;
import zjl.example.com.regularone.bean.NavCategory;
import zjl.example.com.regularone.bean.PhotoGirl;


/**
 * des:图片列表contract
 * Created by xsf
 * on 2016.09.14:38
 */
public interface NavigationContract {
    interface Model extends BaseModel {
        Observable<List<NavCategory>> getNavCategory();
    }

    interface View extends BaseView {
        void returnNavigationData(List<NavCategory> navCategories);
    }
    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void getNavigationDataRequest();
    }
}
