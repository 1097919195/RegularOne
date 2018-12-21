package zjl.example.com.regularone.widget;

import android.content.Context;
import android.util.AttributeSet;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 *该方法在适配器中没使用FlexboxLayout添加textview的时候不可以，底部会出现很多白色，可能是引用不当的问题
 */
public class NestedStickyListHeadersListView extends StickyListHeadersListView {
    public NestedStickyListHeadersListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //测量的大小由一个32位的数字表示，前两位表示测量模式，后30位表示大小，这里需要右移两位才能拿到测量的大小
        int heightSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightSpec);
    }

}
