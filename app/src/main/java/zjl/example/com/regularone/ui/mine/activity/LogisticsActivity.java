package zjl.example.com.regularone.ui.mine.activity;

import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.commonutils.ToastUtil;

import zjl.example.com.regularone.R;
import zjl.example.com.regularone.app.BaseUIActivity;
import zjl.example.com.regularone.bean.LogisticsData;
import zjl.example.com.regularone.ui.mine.contract.LogisticsContract;
import zjl.example.com.regularone.ui.mine.model.LogisticsModel;
import zjl.example.com.regularone.ui.mine.presenter.LogisticsPresenter;

public class LogisticsActivity extends BaseUIActivity<LogisticsPresenter, LogisticsModel> implements LogisticsContract.View {
    @Override
    public int getLayoutId() {
        return R.layout.act_logistics;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void initView() {
        mPresenter.getLogisticsDataRequest();
    }

    @Override
    public void returnLogisticsData(LogisticsData logisticsData) {
        ToastUtil.showShort("OK");
    }

    @Override
    public void showLoading(String title) {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showErrorTip(String msg) {
        ToastUtil.showShort(msg);
    }
}
