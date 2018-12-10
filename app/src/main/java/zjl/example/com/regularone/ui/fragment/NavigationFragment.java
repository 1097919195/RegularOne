package zjl.example.com.regularone.ui.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jaydenxiao.common.base.BaseFragment;
import com.jaydenxiao.common.baserx.RxBus2;
import com.jaydenxiao.common.commonutils.LogUtils;

import java.util.List;

import butterknife.BindView;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import zjl.example.com.regularone.R;
import zjl.example.com.regularone.app.AppConstant;
import zjl.example.com.regularone.bean.NavCategory;
import zjl.example.com.regularone.ui.navigation.adapter.ContentListAdapter;
import zjl.example.com.regularone.ui.navigation.contract.NavigationContract;
import zjl.example.com.regularone.ui.navigation.model.NavigationModel;
import zjl.example.com.regularone.ui.navigation.presenter.NavigationPresenter;

/**
 * Created by Administrator on 2018/11/10 0010.
 */

public class NavigationFragment extends BaseFragment<NavigationPresenter, NavigationModel> implements NavigationContract.View {
    @BindView(R.id.contentList)
    StickyListHeadersListView contentListView;
    @BindView(R.id.typeList)
    RecyclerView typeListView;

    private ContentListAdapter contentListAdapter;
    private BaseQuickAdapter<NavCategory, BaseViewHolder> typeAdapter;
    private boolean startLinkage;

    @Override
    protected int getLayoutResource() {
        return R.layout.frag_navigation;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    protected void initView() {
        //todo 需要判断一下如果还没加载成功就不显示底层的视图
        mPresenter.getNavigationDataRequest();
    }

    //双联动内容加载显示
    @Override
    public void returnNavigationData(List<NavCategory> navCategories) {
        if (navCategories != null) {
            navCategories.get(0).setSelected(true);//默认选中第一条
            //左边的标题类型
            typeAdapter = new BaseQuickAdapter<NavCategory, BaseViewHolder>(R.layout.item_navigation_type_list, navCategories) {
                @Override
                protected void convert(BaseViewHolder helper, NavCategory item) {
                    TextView textView = helper.getView(R.id.title_type);
                    textView.setText(item.getName());

                    textView.setOnClickListener(v -> {
                        contentListView.setSelection(helper.getLayoutPosition());
                        navCategories.get(getTypeAdapterSelectedPostion()).setSelected(false);
                        navCategories.get(helper.getLayoutPosition()).setSelected(true);

                        notifyDataSetChanged();
                    });

                    if (item.isSelected()) {
                        textView.setTextColor(getResources().getColor(R.color.colorPrimary));
                    } else {
                        textView.setTextColor(getResources().getColor(R.color.black));
                    }
                }

                //获取当前选中的item的索引
                private int getTypeAdapterSelectedPostion() {
                    for (int i = 0; i < navCategories.size(); i++) {
                        if (typeAdapter.getData().get(i).isSelected()) {
                            return i;
                        }
                    }
                    return 0;
                }
            };



            typeListView.setAdapter(typeAdapter);
            typeListView.setLayoutManager(new LinearLayoutManager(getActivity()));
            //右边的内容
            contentListAdapter = new ContentListAdapter(getActivity(), navCategories);
            contentListView.setAdapter(contentListAdapter);
            contentListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    //防止调用setSelection的时候滚动数据关联的问题
                    switch(scrollState) {
                        case SCROLL_STATE_TOUCH_SCROLL:// 手指触屏拉动准备滚动，只触发一次        顺序: 1
                            startLinkage = true;
                            break;
                        case SCROLL_STATE_FLING:// 持续滚动开始，只触发一次                顺序: 2
                            break;
                        case SCROLL_STATE_IDLE:// 整个滚动事件结束，只触发一次            顺序: 4
                            startLinkage = false;
                            break;
                        default:
                            break;
                    }

                }

                // 一直在滚动中，多次触发                          顺序: 3
                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if (startLinkage) {
                        NavCategory item = navCategories.get(firstVisibleItem);
                        //当前选中索引和滑动显示的最前面的索引不一致的时候触发
                        if (!item.isSelected()) {
                            navCategories.get(getTypeAdapterSelectedPostion()).setSelected(false);
                            item.setSelected(true);
                            typeAdapter.notifyDataSetChanged();
                            //滑动到选中的item处
                            typeListView.smoothScrollToPosition(getSelectedTypePosition());
                        }
                    }
                }

                //获取当前选中的item的索引
                private int getTypeAdapterSelectedPostion() {
                    for (int i = 0; i < navCategories.size(); i++) {
                        if (typeAdapter.getData().get(i).isSelected()) {
                            return i;
                        }
                    }
                    return 0;
                }

                //获取最终选中的索引
                private int getSelectedTypePosition() {
                    for (int i = 0; i < navCategories.size(); i++) {
                        if (navCategories.get(i).isSelected()) {
                            return i;
                        }
                    }
                    return 0;
                }
            });

            //这里使用onTouch事件来判断更好（根据OnScroll监听item滑动的距离还是没有那么精确，万一item较高就会有问题）
            //注意这里的显示隐藏底部导航和嵌套滑动监听的状态显示问题还需要兼容的（使用的话把下面的注释取消就好了）
            contentListView.setOnTouchListener(new View.OnTouchListener() {
                float firstY = 0;
                @Override
                public boolean onTouch(View v, MotionEvent event) {
//                        switch (event.getAction()){
//                            case MotionEvent.ACTION_DOWN:
//                                firstY=event.getY();
//                                break;
//                            case MotionEvent.ACTION_MOVE:
//                                float moveY = event.getY();
//                                Log.e("moveY_START",moveY+"");
//                                //往上滑动值会变小
//                                if(moveY < firstY-50){
//                                    RxBus2.getInstance().post(AppConstant.MENU_SHOW_HIDE,false);
//                                    firstY = moveY;//事件触发之后及时记录位置方便下次判断
//                                }
//                                if (moveY > firstY+50){
//                                    RxBus2.getInstance().post(AppConstant.MENU_SHOW_HIDE,true);
//                                    firstY = moveY;
//                                }
//                                break;
//                        }
                    return false;
                }
            });
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

    }
}
