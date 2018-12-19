package zjl.example.com.regularone.ui.fragment;

import android.view.View;
import android.widget.LinearLayout;

import com.jaydenxiao.common.base.BaseFragmentLazy;
import com.jaydenxiao.common.commonutils.LogUtils;
import com.jaydenxiao.common.commonutils.ToastUtil;

import butterknife.BindView;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import zjl.example.com.regularone.R;
import zjl.example.com.regularone.bean.VideoData;
import zjl.example.com.regularone.ui.video.contract.VideoContract;
import zjl.example.com.regularone.ui.video.module.VideoModule;
import zjl.example.com.regularone.ui.video.presenter.VideoPresenter;

public class VideoFragment extends BaseFragmentLazy<VideoPresenter,VideoModule> implements VideoContract.View {

    @BindView(R.id.ll_container)
    LinearLayout container;
    @BindView(R.id.videoplayer)
    JzvdStd videoplayer;
    @Override
    protected void lazyLoadData() {
        mPresenter.getVideoDataRequest();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.frag_video;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this,mModel);
    }

    @Override
    protected void initView() {
    }

    @Override
    public void returnVideoData(VideoData videoData) {
        container.setVisibility(View.VISIBLE);
        ToastUtil.showShort("video加载成功");

//        videoplayer.setUp(videoData.getResults().get(0).getUrl(), "饺子闭眼睛", Jzvd.SCREEN_WINDOW_NORMAL);
        videoplayer.setUp("http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4"
                , "饺子闭眼睛", Jzvd.SCREEN_WINDOW_NORMAL);
//        videoplayer.thumbImageView.setImage("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640");
    }

    @Override
    public void showLoading(String title) {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showErrorTip(String msg) {
        ToastUtil.showShort(msg);
    }
}
