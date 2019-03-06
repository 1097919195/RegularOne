package zjl.example.com.regularone.ui.activity.test;

import com.jaydenxiao.common.baserx.RxSchedulers;

import io.reactivex.Observable;
import zjl.example.com.regularone.api.Api;
import zjl.example.com.regularone.api.HostType;

/**
 * Created by asus-pc on 2019/2/21.
 */

public class Model implements Contract.Model {
    @Override
    public Observable<DataBean> getDataInfo() {
        return Api.getDefault(HostType.PHOTO_HOST)
                .getDataInfo("http://api.fm.howugen.com/customer/logList?userStatusId=50&id=18&tdsourcetag=s_pcqq_aiomsg")
                .compose(RxSchedulers.io_main());
    }
}
