package zjl.example.com.regularone.ui.news.fragment;

import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.github.library.BaseRecyclerAdapter;
import com.jaydenxiao.common.base.BaseFragmentLazy;
import com.jaydenxiao.common.commonutils.ImageLoaderUtils;
import com.jaydenxiao.common.commonutils.SPUtils;
import com.jaydenxiao.common.commonutils.ToastUtil;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.footer.LoadingView;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;

import net.moyokoo.diooto.Diooto;
import net.moyokoo.diooto.config.DiootoConfig;
import net.moyokoo.diooto.interfaces.CircleIndexIndicator;
import net.moyokoo.diooto.interfaces.DefaultCircleProgress;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.functions.Consumer;
import zjl.example.com.regularone.R;
import zjl.example.com.regularone.app.AppConstant;
import zjl.example.com.regularone.bean.PhotoGirl;
import zjl.example.com.regularone.ui.news.activity.PhotosDetailActivity;
import zjl.example.com.regularone.ui.news.contract.PhotosListContract;
import zjl.example.com.regularone.ui.news.model.PhotosListModel;
import zjl.example.com.regularone.ui.news.presenter.PhotosListPresenter;

/**
 * Created by Administrator on 2018/10/27 0027.
 */

public class NewsFragment extends BaseFragmentLazy<PhotosListPresenter, PhotosListModel> implements PhotosListContract.View {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.srfLayout)
    TwinklingRefreshLayout srfLayout;
//    @BindView(R.id.srfLayout)
//    SwipeRefreshLayout srfLayout;
    private static int SIZE = 10;
    private int mStartPage = 1;//该请求是从页码1开始的
    List<PhotoGirl> photoGirlList = new ArrayList<>();
    BaseRecyclerAdapter<PhotoGirl> adapter;

    private LinearLayoutManager layoutManager;

    ImageView imageView;
    @Override
    protected void lazyLoadData() {
        //加载初始化数据的时候，数据为空才重新发起请求(防止切回fragment调用该方法进行加载)
        if (adapter.getData().size()<=0){
            mStartPage = 1;
            mPresenter.getPhotosListDataRequest(SIZE, mStartPage);
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.frag_news_list;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    protected void initView() {
//        mPresenter.getPhotosListDataRequest(SIZE, mStartPage);
        initAdapter();
        initSwipRefresh();
        preDialogLoading();
        initRxBus2();
    }

    private void initRxBus2() {
        mRxManager.on(AppConstant.CHANGE_LIST, new Consumer<Boolean>() {
            @Override
            public void accept(Boolean isChecked) throws Exception {
                if (isChecked) {
                    recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                }else {
                    recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
                }
            }
        });
    }

    private void preDialogLoading() {
        imageView = (ImageView) rootView.findViewById(R.id.ivLoadView);
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getDrawable();
        animationDrawable.start();
    }

    private void initSwipRefresh() {
//        srfLayout.setColorSchemeResources(R.color.colorAccent,R.color.colorPrimaryDark);
//        srfLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                mStartPage = 0;
//                mPresenter.getPhotosListDataRequest(SIZE, mStartPage);
//            }
//        });

        srfLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                mStartPage = 1;
                mPresenter.getPhotosListDataRequest(SIZE, mStartPage);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                mPresenter.getPhotosListDataRequest(SIZE, mStartPage);
            }
        });

        //修改刷新类型View
        ProgressLayout headerView = new ProgressLayout(getActivity());
        headerView.setColorSchemeResources(R.color.colorAccent,R.color.colorPrimaryDark);
        srfLayout.setHeaderView(headerView);
        //修改加载类型View
        LoadingView loadingView = new LoadingView(getActivity());
//        loadingView.setColorFilter(R.color.colorPrimaryDark);
//        loadingView.setColorFilter(Color.parseColor("#39C5BB"));//使用上面那种换颜色会错乱
        srfLayout.setBottomView(loadingView);
    }

    private void initAdapter() {
        adapter = new BaseRecyclerAdapter<PhotoGirl>(getActivity(), photoGirlList, R.layout.item_news_main) {
            @Override
            protected void convert(com.github.library.BaseViewHolder helper, PhotoGirl item) {
                ImageView image = helper.getView(R.id.ivGirlImage);
                helper.setText(R.id.tvImageTitle, item.getDesc());
//                Glide.with(getActivity())
//                     .load(item.getUrl())
//                     .into(image);
                ImageLoaderUtils.displayBigPhoto(getActivity(), image, item.getUrl());
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PhotosDetailActivity.startAction(getActivity(),item.getUrl(), image);
                        //这个库使用好像是有问题的(优化好了再说吧)
//                        List<String> photos = new ArrayList<>();
//                        for (int i = 0; i < photoGirlList.size(); i++) {
//                            photos.add(photoGirlList.get(i).getUrl());
//                        }
//                        String[] strings = photos.toArray(new String[photos.size()]);
//
//                        Diooto diooto = new Diooto(getActivity())
//                                .urls(item.getUrl())
//                                //图片或者视频
//                                .type(DiootoConfig.PHOTO)
//                                //点击的位置 如果你的RecyclerView有头部View  则使用 .position(holder.getAdapterPosition(),headSize) headSize为头部布局数量
//                                .position(helper.getAdapterPosition())
//                                //可以传recylcerview自动识别(需要传在item布局中的viewId)  也可以手动传view数组
//                                .views(recyclerView,R.id.ivGirlImage)
//                                //设置选择器 默认CircleIndexIndicator  可实现IIndicator接口自定义
//                                .setIndicator(new CircleIndexIndicator())
//                                //设置进度条样式  默认DefaultProgress 可实现IProgress接口自定义
//                                .setProgress(new DefaultCircleProgress())
//                                //在显示原图之前显示的图片  如果你列表使用Glide加载  这里也使用Glide加载
//                                .loadPhotoBeforeShowBigImage((sketchImageView, position12) -> sketchImageView.displayImage(strings[helper.getAdapterPosition()]))
//                                .start();
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);

        if (SPUtils.getSharedStringData(getActivity(), AppConstant.RANK_LIST).equals("land")) {
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        } else {
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        }
//        layoutManager = new LinearLayoutManager(getActivity());
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
//        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        //检测最后一项加载更多
//        recyclerView.addOnScrollListener(mScrollListener);
    }

    @Override
    public void returnPhotosListData(List<PhotoGirl> photoGirls) {
//        srfLayout.setRefreshing(false);
        imageView.setVisibility(View.INVISIBLE);
        //区别加载和刷新，防止出现界面抖动
        if (mStartPage == 1) {
            srfLayout.finishRefreshing();
            adapter.getData().removeAll(photoGirlList);//刷新数据的时候记得把之前的数据清空
        }else {
            srfLayout.finishLoadmore();
        }
        if (photoGirls != null) {
            mStartPage += 1;

            adapter.getData().addAll(photoGirls);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void scrolltoTop() {
        recyclerView.smoothScrollToPosition(0);
    }

    @Override
    public void showLoading(String title) {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showErrorTip(String msg) {
//        srfLayout.setRefreshing(false);
        if (mStartPage == 1) {
            srfLayout.finishRefreshing();
        }else {
            srfLayout.finishLoadmore();
        }
    }


    //RecyclerView向下滑动事件 https://blog.csdn.net/afanyusong/article/details/51296290
    //(这里是简单的根据linearManager来判断的，其他的布局参考：https://blog.csdn.net/qq402164452/article/details/69374944)
    private RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
        int lastVisibleItem;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == layoutManager.getItemCount()) {
                mPresenter.getPhotosListDataRequest(SIZE, mStartPage);
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//            lastVisibleItem = findLastVisibleItemPosition();
            lastVisibleItem = layoutManager.findLastVisibleItemPosition();
        }
    };

    //查询最后一个可见Item的下标
    public int findLastVisibleItemPosition() {
        int lastVisibleItemPosition = 0;
        lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        return lastVisibleItemPosition;
    }

}
