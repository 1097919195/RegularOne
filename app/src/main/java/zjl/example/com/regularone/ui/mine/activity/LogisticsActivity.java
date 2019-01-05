package zjl.example.com.regularone.ui.mine.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.commonutils.ToastUtil;

import butterknife.BindView;
import zjl.example.com.regularone.R;
import zjl.example.com.regularone.app.BaseUIActivity;
import zjl.example.com.regularone.bean.LogisticsData;
import zjl.example.com.regularone.ui.mine.adapter.LogisticsAdapter;
import zjl.example.com.regularone.ui.mine.contract.LogisticsContract;
import zjl.example.com.regularone.ui.mine.model.LogisticsModel;
import zjl.example.com.regularone.ui.mine.presenter.LogisticsPresenter;

public class LogisticsActivity extends BaseUIActivity<LogisticsPresenter, LogisticsModel> implements LogisticsContract.View {

    @BindView(R.id.timelineRV)
    RecyclerView timelineRV;

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
        if (logisticsData.getData().size()>0) {
            ToastUtil.showShort("默认单号");
            LogisticsAdapter adapter = new LogisticsAdapter(mContext, logisticsData.getData(), R.layout.item_logistics);
            timelineRV.setAdapter(adapter);
            timelineRV.setLayoutManager(new LinearLayoutManager(this));
            adapter.openLoadAnimation(false);
        }else {
            ToastUtil.showShort("默认单号查询失败");
        }

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
