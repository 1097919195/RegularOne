package zjl.example.com.regularone.ui.fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaydenxiao.common.base.BaseFragment;
import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import butterknife.BindView;
import zjl.example.com.regularone.R;
import zjl.example.com.regularone.ui.activity.LoginActivity;
import zjl.example.com.regularone.ui.activity.MainActivity;
import zjl.example.com.regularone.ui.mine.activity.LogisticsActivity;
import zjl.example.com.regularone.ui.mine.activity.StatisticsActivity;
import zjl.example.com.regularone.utils.BaseUIListener;
import zjl.example.com.regularone.utils.Util;

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

    @BindView(R.id.loginOrRegister)
    TextView loginOrRegister;
    @BindView(R.id.logistics_tv)
    TextView logistics_tv;
    @BindView(R.id.statistics_tv)
    TextView statistics_tv;

    Typeface typeface;
    String rmsg;
    @Override
    protected int getLayoutResource() {
        return R.layout.frag_mine;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {
        typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/fzxk.ttf");
        loginOrRegister.setTypeface(typeface);
        logistics_tv.setTypeface(typeface);
        statistics_tv.setTypeface(typeface);
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

    //QQ登录后调用方法更新信息
    public void updataUserInfo() {
        if (LoginActivity.ready(getActivity())) {
            LoginActivity.mInfo.getUserInfo(new IUiListener() {
                @Override
                public void onComplete(Object o) {
                    if (o != null) {
                        JSONObject response = (JSONObject)o;
                        rmsg = response.toString().replace(",", "\n");
                    }
                }

                @Override
                public void onError(UiError uiError) {

                }

                @Override
                public void onCancel() {

                }
            });
        }
    }
}
