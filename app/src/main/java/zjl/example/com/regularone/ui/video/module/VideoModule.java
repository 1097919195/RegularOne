package zjl.example.com.regularone.ui.video.module;

import com.jaydenxiao.common.baserx.RxSchedulers;

import io.reactivex.Observable;
import zjl.example.com.regularone.api.Api;
import zjl.example.com.regularone.api.HostType;
import zjl.example.com.regularone.bean.VideoData;
import zjl.example.com.regularone.ui.video.contract.VideoContract;

public class VideoModule implements VideoContract.Model {
    //http://gank.io/api/data/休息视频/10/1
    //https://gank.io/api/random/data/休息视频/10
    //https://www.apiopen.top/satinApi?type=1&page=1
    @Override
    public Observable<VideoData> getVideoData(int page) {
        return Api.getDefault(HostType.PHOTO_HOST)
                .getVideo("https://www.apiopen.top/satinApi?type=1&page="+page)
                .compose(RxSchedulers.io_main());
    }
}
