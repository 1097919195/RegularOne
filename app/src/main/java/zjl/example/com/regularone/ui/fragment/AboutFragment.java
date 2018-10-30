package zjl.example.com.regularone.ui.fragment;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.view.View;

import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.base.BaseFragment;

import butterknife.BindView;
import util.UpdateAppUtils;
import zjl.example.com.regularone.R;

/**
 * Created by Administrator on 2018/10/30 0030.
 */

public class AboutFragment extends BaseFragment {

    @BindView(R.id.fab_about_shard)
    FloatingActionButton fab_about_shard;
    @BindView(R.id.appData)
    CardView appData;
    @Override
    protected int getLayoutResource() {
        return R.layout.frag_about;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        initShard();
        appData.setOnClickListener(v ->{
            CheckupApp();
        });

    }

    private void initShard() {
        fab_about_shard.setOnClickListener(v -> {
            /**
             * 调用手机默认分享
             */
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share));
            //分享成功后的内容
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_contents, getString(R.string.app_name), "分享的内容"));
            startActivity(Intent.createChooser(intent, "挑选一款应用"));
        });
    }

    private void CheckupApp() {
        UpdateAppUtils.from(getActivity())
                .checkBy(UpdateAppUtils.CHECK_BY_VERSION_NAME) //更新检测方式，默认为VersionCode
                .serverVersionCode(2)
                .serverVersionName("2.0")
                .apkPath("https://www.npclo.com/dlo")
                .showNotification(true) //是否显示下载进度到通知栏，默认为true
                .updateInfo("更新更新")  //更新日志信息 String
                .downloadBy(UpdateAppUtils.DOWNLOAD_BY_APP) //下载方式：app下载、手机浏览器下载。默认app下载
                .isForce(false) //是否强制更新，默认false 强制更新情况下用户不同意更新则不能使用app
                .update();
    }
}
