package zjl.example.com.regularone.ui.activity.test;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.commonutils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import zjl.example.com.regularone.R;

/**
 * Created by asus-pc on 2019/2/21.
 */

public class TestActivity extends BaseActivity<Presenter,Model> implements Contract.View {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private List<DataBean.Beann> dataBeanss = new ArrayList<>();
    private MultiAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.act_test;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this,mModel);
    }

    @Override
    public void initView() {
        initAdapter();
        mPresenter.getDataInfoRequest();
    }

    private void initAdapter() {
        adapter = new MultiAdapter(dataBeanss, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void returnDataInfo(DataBean dataBean) {
        ToastUtil.showShort("请求成功！");
        for (int i=0;i<dataBean.getData().size();i++){
            if (dataBean.getData().get(i).isOnTheLeft()) {
                dataBean.getData().get(i).setItemType(2);
                dataBeanss.add(dataBean.getData().get(i));
            }else {
                dataBean.getData().get(i).setItemType(1);
                if (i == 3) {//模拟右边头像即第二种类型
                    dataBean.getData().get(i).setItemType(2);
                }
                dataBeanss.add(dataBean.getData().get(i));
            }
        }
        adapter.notifyDataSetChanged();
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
