package zjl.example.com.regularone.ui.video.presenter;

import com.jaydenxiao.common.baserx.RxSubscriber2;

import zjl.example.com.regularone.bean.VideoData;
import zjl.example.com.regularone.bean.WeatherInfo;
import zjl.example.com.regularone.ui.video.contract.VideoContract;

public class VideoPresenter extends VideoContract.Presenter {
    @Override
    public void getVideoDataRequest() {
        mRxManage.add(mModel.getVideoData().subscribeWith(new RxSubscriber2<VideoData>(mContext, false) {
            @Override
            protected void _onNext(VideoData videoData) {
                mView.returnVideoData(videoData);
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorTip(message);
            }
        }));
    }
}
