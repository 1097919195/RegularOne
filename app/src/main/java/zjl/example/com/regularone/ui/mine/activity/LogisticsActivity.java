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

import com.jaydenxiao.common.commonutils.KeyBordUtil;
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
    @BindView(R.id.company_type)
    Spinner company_type;
    @BindView(R.id.express_num)
    EditText express_num;
    String company = "yunda";

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
        mPresenter.getLogisticsDataRequest("yunda","3101775486667");//默认的测试单号
        initSpinner();
        initListener();
    }

    private void initListener() {
        //回车键搜索监听
        express_num.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    KeyBordUtil.hideSoftKeyboard(express_num);
                    if (express_num.getEditableText().length() > 0) {
                        mPresenter.getLogisticsDataRequest(company,express_num.getText().toString());
                    }
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
                String[] companys = getResources().getStringArray(R.array.company_spell);
                company = companys[pos];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
    }

    @Override
    public void returnLogisticsData(LogisticsData logisticsData) {
        if (logisticsData.getStatus() == 200) {
            if (logisticsData.getData().size()>0) {
                ToastUtil.showShort("查询成功");
                LogisticsAdapter adapter = new LogisticsAdapter(mContext, logisticsData.getData(), R.layout.item_logistics);
                timelineRV.setAdapter(adapter);
                timelineRV.setLayoutManager(new LinearLayoutManager(this));
                adapter.openLoadAnimation(false);
            }else {
                ToastUtil.showShort("暂无数据");
            }
        }else {
            ToastUtil.showShort(logisticsData.getMessage());
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
