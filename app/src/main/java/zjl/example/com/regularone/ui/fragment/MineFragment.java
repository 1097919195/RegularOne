package zjl.example.com.regularone.ui.fragment;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.widget.LinearLayout;

import com.jaydenxiao.common.base.BaseFragment;

import butterknife.BindView;
import zjl.example.com.regularone.R;
import zjl.example.com.regularone.ui.activity.LoginActivity;
import zjl.example.com.regularone.ui.mine.activity.LogisticsActivity;
import zjl.example.com.regularone.ui.mine.activity.StatisticsActivity;

/**
 * Created by Administrator on 2018/11/8 0008.
 */

public class MineFragment extends BaseFragment {
    @BindView(R.id.login_card)
    CardView login_card;
    @BindView(R.id.logistics_item)
    LinearLayout logistics_item;
    @BindView(R.id.statistics_item)
    LinearLayout statistics_item;
    @Override
    protected int getLayoutResource() {
        return R.layout.frag_mine;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {
        initListener();
    }

    private void initListener() {
        login_card.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        });

        logistics_item.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LogisticsActivity.class);
            startActivity(intent);
        });

        statistics_item.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), StatisticsActivity.class);
            startActivity(intent);
        });
    }
}
