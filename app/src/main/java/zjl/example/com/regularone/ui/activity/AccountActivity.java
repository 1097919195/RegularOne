package zjl.example.com.regularone.ui.activity;

import android.content.Intent;
import android.view.View;

import com.jaydenxiao.common.base.BaseActivity;

import zjl.example.com.regularone.R;

/**
 * Created by Administrator on 2018/10/30 0030.
 */

public class AccountActivity extends BaseActivity {
    @Override
    public int getLayoutId() {
        return R.layout.act_account;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {

    }

    public void Login(View view) {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
