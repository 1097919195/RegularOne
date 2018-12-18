package zjl.example.com.regularone.ui.activity;

import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.hjq.bar.TitleBar;
import com.jaydenxiao.common.base.BaseActivity;

import butterknife.BindView;
import zjl.example.com.regularone.R;
import zjl.example.com.regularone.app.BaseUIActivity;
import zjl.example.com.regularone.widget.XCollapsingToolbarLayout;

/**
 * 一般这种形式的作为主界面，点击search后才跳转到EditText的搜索界面
 */
public class SearchActivity extends BaseUIActivity implements XCollapsingToolbarLayout.OnScrimsListener {

    @BindView(R.id.ctl_test_bar)
    XCollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.t_test_title)
    Toolbar mToolbar;
    @BindView(R.id.tb_test_a_bar)
    TitleBar mTitleBar;

    @BindView(R.id.tv_test_address)
    TextView mAddressView;
    @BindView(R.id.tv_test_search)
    TextView mSearchView;

    private ImmersionBar mImmersionBar;//状态栏沉浸

    @Override
    public int getLayoutId() {
        return R.layout.act_search;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        // 给这个ToolBar设置顶部内边距，才能和TitleBar进行对齐(不使用baseActivity中的沉浸状态栏就是因为没有封装设置距离的方法，只能透明显示，直接全部透明title也会上移android:fitsSystemWindows属性可以配合baseactivity中的试试)
        ImmersionBar.setTitleBar(this, mToolbar);
        ImmersionBar.setTitleBar(this, mTitleBar);

        mImmersionBar = ImmersionBar.with(this)
                .statusBarDarkFont(false)    //默认状态栏字体颜色为黑色
                .keyboardEnable(true);  //解决软键盘与底部输入框冲突问题，默认为false，还有一个重载方法，可以指定软键盘mode
        mImmersionBar.init();//注意场所切换时状态需要重新init，参考工程模板的搜索fragment
        initListener();

//        mSwipeBackHelper.setSwipeBackEnable(false);//可以禁止右滑结束界面
    }

    private void initListener() {
        //设置渐变监听
        mCollapsingToolbarLayout.setOnScrimsListener(this);
    }

    @Override
    public void onScrimsStateChange(boolean shown) {
        // CollapsingToolbarLayout 发生了渐变
        if (shown) {
            mAddressView.setTextColor(getResources().getColor(R.color.black));
            mSearchView.setBackgroundResource(R.drawable.bg_home_search_bar_gray);
            mImmersionBar.statusBarDarkFont(true).init();
        }else {
            mAddressView.setTextColor(getResources().getColor(R.color.white));
            mSearchView.setBackgroundResource(R.drawable.bg_home_search_bar_transparent);
            mImmersionBar.statusBarDarkFont(false).init();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mImmersionBar.destroy();
    }
}
