package zjl.example.com.regularone.ui.mine.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.library.BaseRecyclerAdapter;
import com.github.library.BaseViewHolder;
import com.github.vipulasri.timelineview.TimelineView;

import java.util.List;

import zjl.example.com.regularone.R;
import zjl.example.com.regularone.bean.LogisticsData;

//https://github.com/vipulasri/Timeline-View/blob/master/app/src/main/java/com/github/vipulasri/timelineview/sample/TimeLineAdapter.kt
public class LogisticsAdapter extends BaseRecyclerAdapter<LogisticsData.DataBean> {

    List<LogisticsData.DataBean> datas;
    private Context mContext;

    public LogisticsAdapter(Context context, List<LogisticsData.DataBean> datas, int layoutResId) {
        super(context, datas, layoutResId);
        this.mContext = context;
        this.datas = datas;
    }

    //获取TimeLineView的类型（对应initLine）
    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }

    @Override
    protected void convert(BaseViewHolder helper, LogisticsData.DataBean item) {
        TimelineView timelineView = helper.getView(R.id.timeline);
        TextView data = helper.getView(R.id.text_timeline_date);
        TextView content = helper.getView(R.id.text_timeline_title);
        data.setText(item.getTime());
        content.setText(item.getContext());
        timelineView.initLine(getItemViewType(helper.getLayoutPosition() - getHeaderViewCount()));//记得要initLine一下
        if (item.getTime().equals("2018-05-24 08:40:05")) {
            timelineView.setMarker(ContextCompat.getDrawable(mContext, R.drawable.logistics),R.color.black);//这个设置颜色方式好像会错乱，可能哪里使用有问题
        }
        if (item.getTime().equals("2018-05-23 16:44:25")) {
            timelineView.setMarker(ContextCompat.getDrawable(mContext, R.drawable.logistics),Color.parseColor("#39C5BB"));
        }
    }

    //这里使用了第三方的适配器就直接拿过来用就好了，省的自己定义了
//    class TimeLineViewHolder extends BaseViewHolder {
//        public TimelineView mTimelineView;
//        public TextView content;
//
//        public TimeLineViewHolder(View itemView, int viewType) {
//            super(itemView);
//            mTimelineView = (TimelineView) itemView.findViewById(R.id.timeline);
//            content = itemView.findViewById(R.id.content);
//            mTimelineView.initLine(viewType);
//        }
//    }
}
