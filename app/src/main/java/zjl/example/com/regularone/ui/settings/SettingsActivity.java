package zjl.example.com.regularone.ui.settings;

import android.support.v7.app.ActionBar;

import com.jaydenxiao.common.base.BaseActivity;

import zjl.example.com.regularone.R;


public class SettingsActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.act_settings;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.toolbar_title_setting);
        }

        getFragmentManager().beginTransaction()
                .add(R.id.fragment_container, new SettingsFragment())
                .commit();
    }
}
