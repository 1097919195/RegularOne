package zjl.example.com.regularone.ui.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.jaydenxiao.common.base.BaseFragment;
import com.jaydenxiao.common.base.BaseFragmentAdapter;
import com.jaydenxiao.common.baserx.RxBus2;
import com.jaydenxiao.common.commonutils.SPUtils;
import com.jaydenxiao.common.commonutils.ToastUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import zjl.example.com.regularone.R;
import zjl.example.com.regularone.app.AppApplication;
import zjl.example.com.regularone.app.AppConstant;
import zjl.example.com.regularone.ui.news.fragment.NewsFragment;
import zjl.example.com.regularone.utils.MyUtils;

import static android.support.design.widget.TabLayout.MODE_FIXED;
import static android.support.design.widget.TabLayout.MODE_SCROLLABLE;

/**
 * Created by Administrator on 2018/10/27 0027.
 */

public class NewsMainFragment extends BaseFragment{
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.rank_item)
    ImageView rankItem;
    private BaseFragmentAdapter fragmentAdapter;

    @Override
    protected int getLayoutResource() {
        return R.layout.frag_news_main;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {
        initListener();

        List<String> channelNames = Arrays.asList(AppApplication.getAppContext().getResources().getStringArray(R.array.news_channel_name_static));
        List<Fragment> mNewsFragmentList = new ArrayList<>();
        for (int i = 0; i < channelNames.size(); i++) {
            mNewsFragmentList.add(createListFragments(channelNames.get(i)));
        }
        if(fragmentAdapter==null) {
            fragmentAdapter = new BaseFragmentAdapter(getChildFragmentManager(), mNewsFragmentList, channelNames);
        }else{
            //刷新fragment
            fragmentAdapter.setFragments(getChildFragmentManager(),mNewsFragmentList,channelNames);
        }
        viewPager.setAdapter(fragmentAdapter);
        //建立关联
        tabLayout.setupWithViewPager(viewPager);
        //设置tabs根据容器大小的排列样式
//        tabLayout.setTabMode(MODE_FIXED);
        MyUtils.dynamicSetTabLayoutMode(tabLayout);
    }

    private void initListener() {
        if (SPUtils.getSharedStringData(getActivity(), AppConstant.RANK_LIST).equals("land")) {
            rankItem.setImageResource(R.drawable.rank_land);
        }
        rankItem.setOnClickListener(v -> {
            ToastUtil.showShort("切换排列方式");
            if (!SPUtils.getSharedStringData(getActivity(), AppConstant.RANK_LIST).equals("land")) {
                SPUtils.setSharedStringData(getActivity(),AppConstant.RANK_LIST,"land");
                rankItem.setImageResource(R.drawable.rank_land);
                RxBus2.getInstance().post(AppConstant.CHANGE_LIST, true);
            } else {
                SPUtils.setSharedStringData(getActivity(),AppConstant.RANK_LIST,"port");
                rankItem.setImageResource(R.drawable.rank_port);
                RxBus2.getInstance().post(AppConstant.CHANGE_LIST, false);
            }
        });
    }

    private NewsFragment createListFragments(String title) {
        NewsFragment fragment = new NewsFragment();

        return fragment;
    }
}
