package zjl.example.com.regularone.ui.news.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.jaydenxiao.common.commonutils.LogUtils;
import com.jaydenxiao.common.commonutils.ToastUtil;
import com.jaydenxiao.common.commonwidget.StatusBarCompat;
import com.jaydenxiao.common.security.Md5Security;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.uuzuche.lib_zxing.activity.CaptureActivity;


import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;
import zjl.example.com.regularone.R;
import zjl.example.com.regularone.app.AppConstant;
import zjl.example.com.regularone.ui.activity.MainActivity;
import zjl.example.com.regularone.utils.MyUtils;
import zjl.example.com.regularone.utils.SystemUiVisibilityUtil;
import zjl.example.com.regularone.widget.PullBackLayout;

/**
 * des:大图详情
 * Created by xsf
 * on 2016.09.14:35
 */
public class PhotosDetailActivity extends AppCompatActivity implements PullBackLayout.Callback {


    @BindView(R.id.photo_touch_iv)
    PhotoView photoTouchIv;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.background)
    RelativeLayout background;
    @BindView(R.id.pull_back_layout)
    PullBackLayout pullBackLayout;
    private boolean mIsToolBarHidden;
    private boolean mIsStatusBarHidden;
    String photoDetailUrl;
    private ColorDrawable mBackground;//将原来的背景切换成我们自己通过ColorDrawable画的背景，除了画的部分，其他地方全部都是黑色填充的。
    private Unbinder unbinder;

    public static final File PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);//获得外部存储器的第一层的文件对象
    private File storageFile;
    private File outputImage;
    private Uri imageUri;


    public static void startAction(Context context, String url, View view){
        Intent intent = new Intent(context, PhotosDetailActivity.class);
        intent.putExtra(AppConstant.PHOTO_DETAIL,url);
        //无过渡动画
        context.startActivity(intent);
        //有过渡动画
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            ActivityOptions options = ActivityOptions
//                    .makeSceneTransitionAnimation((Activity) context,view, AppConstant.TRANSITION_ANIMATION_NEWS_PHOTOS);
//            context.startActivity(intent, options.toBundle());
//        } else {
//            //让新的Activity从一个小的范围扩大到全屏
//            ActivityOptionsCompat options = ActivityOptionsCompat
//                    .makeScaleUpAnimation(view, view.getWidth() / 2, view.getHeight() / 2, 0, 0);
//            ActivityCompat.startActivity((Activity) context, intent, options.toBundle());
//        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.translucentStatusBar(this);
        setContentView(R.layout.act_photo_detail);
        unbinder = ButterKnife.bind(this);//绑定activity
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    public void initView() {
        pullBackLayout.setCallback(this);//实现回调
        toolBarFadeIn();
        initToolbar();
        initBackground();
        loadPhotoIv();
        setPhotoViewClickEvent();
        initFileStore();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);//添加在该方法才会展示图标
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();//支持过渡动画
                } else {
                    finish();
                }
            }
        });
    }

    private void loadPhotoIv() {
        photoDetailUrl = getIntent().getStringExtra(AppConstant.PHOTO_DETAIL);
        Glide.with(this).load(photoDetailUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(com.jaydenxiao.common.R.drawable.ic_empty_picture)
                .crossFade().into(photoTouchIv);
    }

    //图片点击事件
    private void setPhotoViewClickEvent() {
        photoTouchIv.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float v, float v1) {
                //取消toolbar和沉浸栏
                hideOrShowToolbar();
                hideOrShowStatusBar();
            }
        });
    }

    private void initBackground() {
        mBackground = new ColorDrawable(Color.BLACK);
        MyUtils.getRootView(this).setBackground(mBackground);
    }


    protected void hideOrShowToolbar() {
        toolbar.animate()
                .alpha(mIsToolBarHidden ? 1.0f : 0.0f)
                .setInterpolator(new DecelerateInterpolator(2))
                .start();
        mIsToolBarHidden = !mIsToolBarHidden;
    }

    private void hideOrShowStatusBar() {
        if (mIsStatusBarHidden) {
            SystemUiVisibilityUtil.enter(PhotosDetailActivity.this);
        } else {
            SystemUiVisibilityUtil.exit(PhotosDetailActivity.this);
        }
        mIsStatusBarHidden = !mIsStatusBarHidden;
    }

    private void toolBarFadeIn() {
        mIsToolBarHidden = true;
        hideOrShowToolbar();
    }

    private void toolBarFadeOut() {
        mIsToolBarHidden = false;
        hideOrShowToolbar();
    }

    @Override
    public void onPullStart() {
        toolBarFadeOut();

        mIsStatusBarHidden = true;
        hideOrShowStatusBar();
    }

    //调用的函数
    @Override
    public void onPull(float progress1) {
        float progress = Math.abs(progress1);//保证手指上滑的时候也能正常的使背景透明
        progress = Math.min(1f, progress * 3f);
        mBackground.setAlpha((int) (0xff/*255*/ * (1f - progress)));
    }

    @Override
    public void onPullCancel() {
        toolBarFadeIn();
    }

    @Override
    public void onPullComplete() {
        supportFinishAfterTransition();
    }

    @Override
    public void supportFinishAfterTransition() {
        super.supportFinishAfterTransition();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu_download, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.download) {
            startDownLoad(photoDetailUrl);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initFileStore() {
        storageFile = new File(PATH.getAbsoluteFile() + File.separator + "beautifulGirl");
        if (!storageFile.isDirectory()) {//创建目录
            storageFile.mkdirs();
        }
    }

    private void startDownLoad(String url) {
        String name = Md5Security.getMD5(url);
        outputImage = new File(storageFile, name+".jpg");
        //下载的图片测试地址 http://cdn.llsapp.com/crm_test_1449051526097.jpg
        //下载的视频测试地址 http://flv3.bn.netease.com/tvmrepo/2018/6/H/9/EDJTRBEH9/SD/EDJTRBEH9-mobile.mp4
        FileDownloader.getImpl()
                .create(url)//下载apk需要服务端支持，避免解析失败
                .setPath(outputImage.toString())
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        int percent = (int) ((double) soFarBytes / (double) totalBytes * 100);
                        ToastUtil.showShort(String.valueOf(percent) + "%");
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        ToastUtil.showShort("100%");
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        LogUtils.loge(e.getMessage());
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                        continueDownLoad(task);//如果存在了相同的任务，那么就继续下载
                    }
                }).start();

        //fixme 每次的imageUri都是新的，可能没能及时通知相册更新，多点击会有效果的（可改进）
        imageUri = Uri.fromFile(outputImage);
        Intent intentBc1 = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intentBc1.setData(imageUri);
        this.sendBroadcast(intentBc1);
    }

    private void continueDownLoad(BaseDownloadTask task) {
        while (task.getSmallFileSoFarBytes() != task.getSmallFileTotalBytes()) {
            int percent = (int) ((double) task.getSmallFileSoFarBytes() / (double) task.getSmallFileTotalBytes() * 100);
            ToastUtil.showShort(String.valueOf(percent) + "%");
        }
    }

}
