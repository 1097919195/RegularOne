package zjl.example.com.regularone.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import com.jaydenxiao.common.baserx.RxBus;

import zjl.example.com.regularone.app.AppConstant;

//快速滑动的一段时间内是无法点击的，或者需要双击
@SuppressWarnings("unused")
public class BottomNavigationViewBehavior extends CoordinatorLayout.Behavior<BottomNavigationView> {

    //用来在代码里实例化Behavior的构造
    public BottomNavigationViewBehavior() {
    }

    //如果要在布局中使用Behavior，这个构造是必须的
    public BottomNavigationViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(
            @NonNull CoordinatorLayout coordinatorLayout,
            @NonNull BottomNavigationView child,
            @NonNull View directTargetChild,
            @NonNull View target, int axes, int type) {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedPreScroll(
            @NonNull CoordinatorLayout coordinatorLayout,
            @NonNull BottomNavigationView child,
            @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
        child.setTranslationY(
                Math.max(0f, Math.min(child.getHeight(), child.getTranslationY() + dy))
        );
    }
}
