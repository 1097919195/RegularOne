package zjl.example.com.regularone.ui.fragment;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.github.library.BaseRecyclerAdapter;
import com.github.library.BaseViewHolder;
import com.jaydenxiao.common.base.BaseFragmentLazy;
import com.jaydenxiao.common.commonutils.ImageLoaderUtils;
import com.jaydenxiao.common.commonutils.ToastUtil;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.footer.LoadingView;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import zjl.example.com.regularone.R;
import zjl.example.com.regularone.bean.VideoData;
import zjl.example.com.regularone.ui.video.contract.VideoContract;
import zjl.example.com.regularone.ui.video.module.VideoModule;
import zjl.example.com.regularone.ui.video.presenter.VideoPresenter;

public class VideoFragment extends BaseFragmentLazy<VideoPresenter, VideoModule> implements VideoContract.View {

    @BindView(R.id.video_container)
    FrameLayout container;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.srfLayout)
    TwinklingRefreshLayout srfLayout;

    private static int SIZE = 10;
    private int mStartPage = 0;
    List<VideoData.DataBean> videoDataList = new ArrayList<>();
    BaseRecyclerAdapter<VideoData.DataBean> adapter;

    @Override
    protected void lazyLoadData() {
        container.setVisibility(View.GONE);
        mPresenter.getVideoDataRequest();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.frag_video;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    protected void initView() {
        initSwipRefresh();
        initVideoAdapter();
    }

    private void initSwipRefresh() {
        srfLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                mStartPage = 0;
                mPresenter.getVideoDataRequest();
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                mPresenter.getVideoDataRequest();
            }
        });

        //修改刷新类型View
        ProgressLayout headerView = new ProgressLayout(getActivity());
        headerView.setColorSchemeResources(R.color.colorAccent,R.color.colorPrimaryDark);
        srfLayout.setHeaderView(headerView);
        //修改加载类型View
        LoadingView loadingView = new LoadingView(getActivity());
//        loadingView.setColorFilter(Color.parseColor("#39C5BB"));
        srfLayout.setBottomView(loadingView);
    }

    private void initVideoAdapter() {
        adapter = new BaseRecyclerAdapter<VideoData.DataBean>(getActivity(), videoDataList, R.layout.item_video_list) {
            @Override
            protected void convert(BaseViewHolder helper, VideoData.DataBean item) {
                JzvdStd jzvdStd = helper.getView(R.id.videoplayer);

                jzvdStd.setUp(item.getVideouri(), item.getText(), Jzvd.SCREEN_WINDOW_NORMAL);
                ImageLoaderUtils.displaySmallPhoto(getContext(),jzvdStd.thumbImageView,item.getBimageuri());
//                jzvdStd.setUp("http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4"
//                        , "饺子闭眼睛", Jzvd.SCREEN_WINDOW_NORMAL);
//                jzvdStd.thumbImageView.setImage("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640");
            }
        };
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void returnVideoData(VideoData videoData) {
        container.setVisibility(View.VISIBLE);

        srfLayout.finishRefreshing();
        srfLayout.finishLoadmore();
        if (videoData != null) {
            mStartPage += 1;
            adapter.getData().addAll(videoData.getData());
            adapter.notifyDataSetChanged();
        }

//        videoplayer.setUp(videoData.getResults().get(0).getUrl(), "饺子闭眼睛", Jzvd.SCREEN_WINDOW_NORMAL);
//        videoplayer.setUp("http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4"
//                , "饺子闭眼睛", Jzvd.SCREEN_WINDOW_NORMAL);
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
        srfLayout.finishRefreshing();
        srfLayout.finishLoadmore();
        ToastUtil.showShort(msg);
    }
}