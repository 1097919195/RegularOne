package zjl.example.com.regularone.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.jaydenxiao.common.base.BaseActivity;

import butterknife.BindView;
import zjl.example.com.regularone.R;

/**
 * Created by Administrator on 2018/10/30 0030.
 */

public class LoginActivity extends BaseActivity {
    @BindView(R.id.link_signup)
    TextView link_signup;
    @Override
    public int getLayoutId() {
        return R.layout.act_login;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        initListener();
    }

    private void initListener() {
        link_signup.setOnClickListener(v -> {

        });
    }

    public void Login(View view) {
        finish();
    }
}
