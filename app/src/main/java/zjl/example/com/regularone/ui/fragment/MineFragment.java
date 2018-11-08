package zjl.example.com.regularone.ui.fragment;

import android.content.Intent;
import android.support.v7.widget.CardView;

import com.jaydenxiao.common.base.BaseFragment;

import butterknife.BindView;
import zjl.example.com.regularone.R;
import zjl.example.com.regularone.ui.activity.LoginActivity;

/**
 * Created by Administrator on 2018/11/8 0008.
 */

public class MineFragment extends BaseFragment {
    @BindView(R.id.login_card)
    CardView login_card;
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
    }
}
