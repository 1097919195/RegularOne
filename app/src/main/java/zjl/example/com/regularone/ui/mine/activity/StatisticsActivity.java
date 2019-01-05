package zjl.example.com.regularone.ui.mine.activity;

import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.commonutils.ToastUtil;

import zjl.example.com.regularone.R;
import zjl.example.com.regularone.app.BaseUIActivity;
import zjl.example.com.regularone.bean.StatisticsData;
import zjl.example.com.regularone.ui.mine.contract.StatisticsContract;
import zjl.example.com.regularone.ui.mine.model.StatisticsModel;
import zjl.example.com.regularone.ui.mine.presenter.StatisticsPresenter;

public class StatisticsActivity extends BaseUIActivity<StatisticsPresenter, StatisticsModel>  implements StatisticsContract.View{
    @Override
    public int getLayoutId() {
        return R.layout.act_statistics;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void initView() {
        mPresenter.getStatisticsDataRequest();
    }

    @Override
    public void returnStatisticsData(StatisticsData statisticsData) {
        ToastUtil.showShort("统计成功");
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
