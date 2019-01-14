package zjl.example.com.regularone.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jaydenxiao.common.base.BaseActivity;

import butterknife.BindView;
import zjl.example.com.regularone.R;
import zjl.example.com.regularone.utils.EditTextInputHelper;

/**
 * Created by Administrator on 2018/10/30 0030.
 */

public class LoginActivity extends BaseActivity {
    @BindView(R.id.link_signup)
    TextView link_signup;
    @BindView(R.id.sign_in_button)
    Button sign_in_button;
    @BindView(R.id.username_input)
    TextInputEditText username_input;
    @BindView(R.id.password_input)
    TextInputEditText password_input;

    private EditTextInputHelper mEditTextInputHelper;

    @Override
    public int getLayoutId() {
        return R.layout.act_login;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        //设置登录按钮不可点击
//        sign_in_button.setBackgroundColor(getColor(R.color.gray));
//        sign_in_button.setClickable(false);
        mEditTextInputHelper = new EditTextInputHelper(sign_in_button, false);
        mEditTextInputHelper.addViews(username_input, password_input);
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
