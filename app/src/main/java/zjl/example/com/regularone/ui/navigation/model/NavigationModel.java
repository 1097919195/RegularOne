package zjl.example.com.regularone.ui.navigation.model;

import com.jaydenxiao.common.baserx.RxSchedulers;

import java.util.List;

import io.reactivex.Observable;
import zjl.example.com.regularone.api.Api;
import zjl.example.com.regularone.api.HostType;
import zjl.example.com.regularone.bean.HttpResponse;
import zjl.example.com.regularone.bean.NavCategory;
import zjl.example.com.regularone.ui.navigation.contract.NavigationContract;

/**
 * Created by Administrator on 2018/11/12 0012.
 */

public class NavigationModel implements NavigationContract.Model {
    @Override
    public Observable<List<NavCategory>> getNavCategory() {
        return Api.getDefault(HostType.ARTICLE_HOST)
                .getNavCategories()
                .map(new Api.HttpResponseFunc<>())
                .compose(RxSchedulers.io_main());
    }
}
