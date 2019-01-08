package zjl.example.com.regularone.ui.mine.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.commonutils.KeyBordUtil;
import com.jaydenxiao.common.commonutils.ToastUtil;

import butterknife.BindView;
import zjl.example.com.regularone.R;
import zjl.example.com.regularone.app.BaseUIActivity;
import zjl.example.com.regularone.bean.LogisticsData;
import zjl.example.com.regularone.ui.activity.MainActivity;
import zjl.example.com.regularone.ui.mine.adapter.LogisticsAdapter;
import zjl.example.com.regularone.ui.mine.contract.LogisticsContract;
import zjl.example.com.regularone.ui.mine.model.LogisticsModel;
import zjl.example.com.regularone.ui.mine.presenter.LogisticsPresenter;
import zjl.example.com.regularone.utils.MyUtils;

public class LogisticsActivity extends BaseUIActivity<LogisticsPresenter, LogisticsModel> implements LogisticsContract.View {

    @BindView(R.id.timelineRV)
    RecyclerView timelineRV;
    @BindView(R.id.company_type)
    Spinner company_type;
    @BindView(R.id.express_num)
    EditText express_num;

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
        initSpinner();
        initListener();
    }

    private void initListener() {
        express_num.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    KeyBordUtil.hideSoftKeyboard(express_num);
                    mPresenter.getLogisticsDataRequest();
                    return true;
                }
                return false;
            }
        });
    }

    private void initSpinner() {
        company_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//                String[] languages = getResources().getStringArray(R.array.company);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
    }

    @Override
    public void returnLogisticsData(LogisticsData logisticsData) {
        if (logisticsData.getData().size()>0) {
            ToastUtil.showShort("测试单号");
            LogisticsAdapter adapter = new LogisticsAdapter(mContext, logisticsData.getData(), R.layout.item_logistics);
            timelineRV.setAdapter(adapter);
            timelineRV.setLayoutManager(new LinearLayoutManager(this));
            adapter.openLoadAnimation(false);
        }else {
            ToastUtil.showShort("测试单号查询失败");
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
