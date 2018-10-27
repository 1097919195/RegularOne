package zjl.example.com.regularone.ui.news.model;


import com.jaydenxiao.common.baserx.RxSchedulers;

import java.util.List;
import io.reactivex.functions.Function;

import io.reactivex.Observable;
import zjl.example.com.regularone.api.Api;
import zjl.example.com.regularone.api.HostType;
import zjl.example.com.regularone.bean.GirlData;
import zjl.example.com.regularone.bean.PhotoGirl;
import zjl.example.com.regularone.ui.news.contract.PhotosListContract;


/**
 * des:图片
 * Created by xsf
 * on 2016.09.12:02
 */
public class PhotosListModel implements PhotosListContract.Model{
    @Override
    public Observable<List<PhotoGirl>> getPhotosListData(int size, int page) {
        return Api.getDefault(HostType.PHOTO_HOST)
                .getPhotoList(Api.getCacheControl(),size, page)
                .map(new Function<GirlData, List<PhotoGirl>>() {
                    @Override
                    public List<PhotoGirl> apply(GirlData girlData) throws Exception {
                        return girlData.getResults();
                    }
                })
                .compose(RxSchedulers.<List<PhotoGirl>>io_main());
    }
}
