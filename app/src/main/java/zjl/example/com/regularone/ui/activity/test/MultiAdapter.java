package zjl.example.com.regularone.ui.activity.test;


import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jaydenxiao.common.commonutils.ImageLoaderUtils;

import java.util.List;

import zjl.example.com.regularone.R;

/**
 * Created by asus-pc on 2019/2/21.
 */

public class MultiAdapter extends BaseMultiItemQuickAdapter<DataBean.Beann, BaseViewHolder> {

    private BaseQuickAdapter<DataBean.Beann.DetailListBean,BaseViewHolder> adapter;
    private Context mContext;

    public MultiAdapter(List<DataBean.Beann> data, Context context) {
        super(data);
        this.mContext = context;
        addItemType(1, R.layout.item_test_left);
        addItemType(2, R.layout.item_test_right);
    }

    @Override
    protected void convert(BaseViewHolder helper, DataBean.Beann item) {
        switch (helper.getItemViewType()) {
            case 1:
                //左
                helper.setText(R.id.time, item.getOperationTime());
                helper.setText(R.id.name, item.getDetailList().get(0).getName());
                ImageView image1 = helper.getView(R.id.left_photo);
                Glide.with(mContext).load(item.getOperationUserStatus().getProfileImage().getThumb200()).into(image1);
                image1.bringToFront();
                //第二个列表
                RecyclerView recyclerViewTwo1 = helper.getView(R.id.rv_data);
                adapter = new BaseQuickAdapter<DataBean.Beann.DetailListBean,BaseViewHolder>(R.layout.item_data_data, item.getDetailList()) {
                    @Override
                    protected void convert(BaseViewHolder helper, DataBean.Beann.DetailListBean item) {
                        if (item.getName() == null || item.getName().equals("")||item.getName().isEmpty()) {
                            helper.setText(R.id.data1, "---");
                        }else {
                            helper.setText(R.id.data1, item.getName());
                        }
                        if (item.getOriginalValue() == null || item.getOriginalValue().equals("")||item.getOriginalValue().isEmpty()) {
                            helper.setText(R.id.data2, "---");
                        }else {
                            helper.setText(R.id.data2, item.getOriginalValue());
                        }
                        if (item.getCurrentValue() == null || item.getCurrentValue().equals("")||item.getCurrentValue().isEmpty()) {
                            helper.setText(R.id.data3, "---");
                        }else {
                            helper.setText(R.id.data3, item.getCurrentValue());
                        }
                    }

                };
                recyclerViewTwo1.setAdapter(adapter);
                recyclerViewTwo1.setLayoutManager(new LinearLayoutManager(mContext));
                break;
            case 2:
                //右
                helper.setText(R.id.time, item.getOperationTime());
                helper.setText(R.id.name, item.getDetailList().get(0).getName());
                ImageView image2 = helper.getView(R.id.right_photo);
                Glide.with(mContext).load(item.getOperationUserStatus().getProfileImage().getThumb200()).into(image2);
                image2.bringToFront();
                //第二个列表
                RecyclerView recyclerViewTwo2 = helper.getView(R.id.rv_data);
                adapter = new BaseQuickAdapter<DataBean.Beann.DetailListBean,BaseViewHolder>(R.layout.item_data_data, item.getDetailList()) {
                    @Override
                    protected void convert(BaseViewHolder helper, DataBean.Beann.DetailListBean item) {
                        if (item.getName() == null || item.getName().equals("")||item.getName().isEmpty()) {
                            helper.setText(R.id.data1, "---");
                        }else {
                            helper.setText(R.id.data1, item.getName());
                        }
                        if (item.getOriginalValue() == null || item.getOriginalValue().equals("")||item.getOriginalValue().isEmpty()) {
                            helper.setText(R.id.data2, "---");
                        }else {
                            helper.setText(R.id.data2, item.getOriginalValue());
                        }
                        if (item.getCurrentValue() == null || item.getCurrentValue().equals("")||item.getCurrentValue().isEmpty()) {
                            helper.setText(R.id.data3, "---");
                        }else {
                            helper.setText(R.id.data3, item.getCurrentValue());
                        }
                    }

                };
                recyclerViewTwo2.setAdapter(adapter);
                recyclerViewTwo2.setLayoutManager(new LinearLayoutManager(mContext));
                break;
        }
    }
}
