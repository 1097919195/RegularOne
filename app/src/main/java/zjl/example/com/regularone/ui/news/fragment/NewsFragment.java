package zjl.example.com.regularone.ui.news.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.library.BaseRecyclerAdapter;
import com.github.library.view.LoadType;
import com.jaydenxiao.common.base.BaseFragment;
import com.jaydenxiao.common.commonutils.ImageLoaderUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import zjl.example.com.regularone.R;
import zjl.example.com.regularone.bean.PhotoGirl;
import zjl.example.com.regularone.ui.news.contract.PhotosListContract;
import zjl.example.com.regularone.ui.news.model.PhotosListModel;
import zjl.example.com.regularone.ui.news.presenter.PhotosListPresenter;

/**
 * Created by Administrator on 2018/10/27 0027.
 */

public class NewsFragment extends BaseFragment<PhotosListPresenter, PhotosListModel> implements PhotosListContract.View {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.srfLayout)
    SwipeRefreshLayout srfLayout;
    private static int SIZE = 20;
    private int mStartPage = 0;
    List<PhotoGirl> photoGirlList = new ArrayList<>();
    BaseRecyclerAdapter<PhotoGirl> adapter;

    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_news_list;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    protected void initView() {
        mPresenter.getPhotosListDataRequest(SIZE, mStartPage);
        initAdapter();
        initSwipRefresh();
    }

    private void initSwipRefresh() {
        layoutManager = new LinearLayoutManager(getActivity());
        srfLayout.setColorSchemeResources(R.color.colorAccent,R.color.colorPrimaryDark);
        srfLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mStartPage = 0;
                mPresenter.getPhotosListDataRequest(SIZE, mStartPage);
            }
        });
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
            }
        };
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
//        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        recyclerView.addOnScrollListener(mScrollListener);
    }

    @Override
    public void returnPhotosListData(List<PhotoGirl> photoGirls) {
        srfLayout.setRefreshing(false);
        if (photoGirls != null) {
            photoGirlList = photoGirls;
            mStartPage += 1;

//            //好像会缓存的
//            if (adapter.getData().size() > 0) {
//                adapter.getData().clear();
//            }
            adapter.getData().addAll(photoGirls);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showLoading(String title) {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showErrorTip(String msg) {
        srfLayout.setRefreshing(false);
    }


    //RecyclerView向下滑动事件
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
            lastVisibleItem = findLastVisibleItemPosition();
        }
    };

    //查询最后一个可见Item的下标
    public int findLastVisibleItemPosition() {
        int lastVisibleItemPosition = 0;
        lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        return lastVisibleItemPosition;
    }

}
