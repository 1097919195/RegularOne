package zjl.example.com.regularone.ui.activity;

import com.gyf.barlibrary.ImmersionBar;

import zjl.example.com.regularone.R;
import zjl.example.com.regularone.app.BaseUIActivity;

public class FavoriteActivity extends BaseUIActivity {
    @Override
    public int getLayoutId() {
        return R.layout.act_favorite;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        ImmersionBar.with(this)
                .statusBarColor(R.color.main_color).init();
    }
}
