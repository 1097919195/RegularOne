package zjl.example.com.regularone.ui.video.contract;

import com.jaydenxiao.common.base.BaseModel;
import com.jaydenxiao.common.base.BasePresenter;
import com.jaydenxiao.common.base.BaseView;

import io.reactivex.Observable;
import zjl.example.com.regularone.bean.VideoData;

public interface VideoContract {
    interface Model extends BaseModel {
        Observable<VideoData> getVideoData(int page);
    }

    interface View extends BaseView {
        void returnVideoData(VideoData videoData);
    }
    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void getVideoDataRequest(int page);
    }
}
