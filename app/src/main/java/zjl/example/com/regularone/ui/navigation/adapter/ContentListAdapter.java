package zjl.example.com.regularone.ui.navigation.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.jaydenxiao.common.commonutils.LogUtils;

import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import zjl.example.com.regularone.R;
import zjl.example.com.regularone.bean.Article;
import zjl.example.com.regularone.bean.NavCategory;
import zjl.example.com.regularone.ui.activity.BrowserActivity;

/**
 * Created by Administrator on 2018/11/12 0012.
 */

public class ContentListAdapter extends BaseAdapter implements StickyListHeadersAdapter {
    private List<NavCategory> mDatas;
    private Context context;

    public ContentListAdapter(Context context, List<NavCategory> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_header_type, parent, false);
        }
        ((TextView) (convertView)).setText(mDatas.get(position).getName());
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
//        LogUtils.loge(String.valueOf(mDatas.get(position).getCid()));
        return mDatas.get(position).getCid();
    }

    @Override
    public int getCount() {
        if (mDatas == null) {
            return 0;
        }
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StickyItemViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_navigation_content_list, parent, false);
            holder = new StickyItemViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (StickyItemViewHolder) convertView.getTag();
        }
        holder.bindData(mDatas.get(position));
//        convert(holder, mDatas.get(position)); 这种封装是一个公共大封装(要写成抽象的方法和类)
        return convertView;
    }

    class StickyItemViewHolder {
        private TextView nav_title;
        private FlexboxLayout flex_box;
        private View itemView;
        public StickyItemViewHolder(View itemView){
            nav_title = itemView.findViewById(R.id.nav_title);
            flex_box = itemView.findViewById(R.id.flex_box);
            this.itemView = itemView;

        }

        public void bindData(NavCategory item) {
            nav_title.setText(item.getName());
            List<Article> articleList = item.getArticles();
            LayoutInflater layoutInflater = LayoutInflater.from(itemView.getContext());
            TextView textView;
            for (int i = 0; i < articleList.size(); i++) {
                final Article article = articleList.get(i);

                textView = (TextView) layoutInflater.inflate(
                        R.layout.textview_tag, null);
                textView.setText(article.getTitle());
                flex_box.addView(textView);

                FlexboxLayout.LayoutParams layoutParams =
                        (FlexboxLayout.LayoutParams) textView.getLayoutParams();
                layoutParams.setMargins(0, 0, 16, 16);
                textView.setLayoutParams(layoutParams);

                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(itemView.getContext(), BrowserActivity.class);
                        intent.putExtra("URL", article.getLink());
                        intent.putExtra("TITLE", article.getTitle());
                        itemView.getContext().startActivity(intent);
                    }
                });
            }
        }


    }
}
