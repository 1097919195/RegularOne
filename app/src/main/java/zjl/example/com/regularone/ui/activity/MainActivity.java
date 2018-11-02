package zjl.example.com.regularone.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.commonutils.ACache;
import com.jaydenxiao.common.commonutils.CollectionUtils;
import com.jaydenxiao.common.commonutils.ImageLoaderUtils;
import com.jaydenxiao.common.commonutils.LogUtils;
import com.jaydenxiao.common.commonutils.ToastUtil;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import util.UpdateAppUtils;
import zjl.example.com.regularone.BuildConfig;
import zjl.example.com.regularone.R;
import zjl.example.com.regularone.app.AppApplication;
import zjl.example.com.regularone.app.AppConstant;
import zjl.example.com.regularone.ui.fragment.AboutFragment;
import zjl.example.com.regularone.ui.fragment.NewsMainFragment;
import zjl.example.com.regularone.utils.FileUtils;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    private static final int REQUEST_CODE = 1000;
    private static final int REQUEST_PHOTO = 1001;
    NewsMainFragment newsMainFragment;
    AboutFragment aboutFragment;

    ImageView photo;

    @Override
    public int getLayoutId() {
        return R.layout.act_main;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        setSupportActionBar(toolbar);
        toolbar.setTitle("首页");

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRxManager.post(AppConstant.NEWS_LIST_TO_TOP, "");
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        photo = headerView.findViewById(R.id.photo);


//        dialogTest();
        loadingViewTest();
//        appDataTest();

        //去掉AppBarLayout下面的阴影
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            findViewById(R.id.action_bar).setOutlineProvider(null);
            findViewById(R.id.toolbar).setOutlineProvider(ViewOutlineProvider.BOUNDS);
        }

        initFragment();
        //设置菜单默认选中项
        navigationView.setCheckedItem(R.id.nav_menu_news);
        initPersonPhoto();
        initListener();
    }

    private void initPersonPhoto() {
        //根据有无缓存 加载显示头像(如果是服务器则需要从服务器获取)
        if (ACache.get(AppApplication.getAppContext()).getAsString(AppConstant.STORE_PERSON_PHOTO) == null) {
            ImageLoaderUtils.displayRound(this, photo, R.drawable.bg_about_land);
        } else {
            ImageLoaderUtils.displayRound(this, photo, ACache.get(AppApplication.getAppContext()).getAsString(AppConstant.STORE_PERSON_PHOTO));
        }
    }

    private void initListener() {
        photo.setOnClickListener(v ->{
            //直接调用系统的打开相册
//            openPicture();
            //使用的图库选择器
            Matisse.from(MainActivity.this)
                    .choose(MimeType.allOf()) // 选择 mime 的类型
                    .countable(true)
                    .capture(true)  // 开启相机，和 captureStrategy 一并使用否则报错
                    .captureStrategy(new CaptureStrategy(true, BuildConfig.APPLICATION_ID + ".MyFileProvider")) // 拍照的图片路径
                    .maxSelectable(9) // 图片选择的最多数量
                    .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                    .thumbnailScale(0.85f) // 缩略图的比例
                    .imageEngine(new GlideEngine()) // 使用的图片加载引擎
                    .forResult(REQUEST_PHOTO); // 设置作为标记的请求码

        });
    }

    //开启图片库
    public void openPicture() {
        //Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT); // "android.intent.action.GET_CONTENT"

        //为了更好的适应版本,4.4前后获取的路径不同
        Intent innerIntent = new Intent();
        if (Build.VERSION.SDK_INT < 19) {
            innerIntent.setAction(Intent.ACTION_GET_CONTENT);
        } else {
            //innerIntent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            innerIntent.setAction(Intent.ACTION_PICK);
        }

        innerIntent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        //弹出的对话框
        Intent wrapperIntent = Intent.createChooser(innerIntent,
                "选择二维码图片");
        this.startActivityForResult(wrapperIntent, REQUEST_PHOTO);
    }

    private void initFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        newsMainFragment = new NewsMainFragment();
        aboutFragment = new AboutFragment();
        transaction.add(R.id.rl_body, newsMainFragment, "newsMainFragment");
        transaction.add(R.id.rl_body, aboutFragment, "aboutFragment");
        transaction.hide(aboutFragment);
        transaction.commit();
    }

    private void loadingViewTest() {
        ImageView imageView = (ImageView) findViewById(R.id.ivLoadView);
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getDrawable();
        animationDrawable.start();
    }

    private void appDataTest() {
        UpdateAppUtils.from(this)
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

    Dialog mLoadingDialog;
    private void dialogTest() {
        View view = LayoutInflater.from(this).inflate(R.layout.loading_dialog, null);
        mLoadingDialog = new Dialog(this,R.style.CustomProgressDialog);
        mLoadingDialog.setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        mLoadingDialog.show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu_qrcode, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            ToastUtil.showShort("扫描二维码");
            //默认的扫描界面
            Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(MainActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
        /**
         * 更换头像（直接调用系统的打开相册）
         */
//        if (requestCode == REQUEST_PHOTO) {
//            if (null != data) {
//                String photoPath = "";
//                // 获取选中图片的路径
//                Cursor cursor = getContentResolver().query(
//                        data.getData(), new String[]{MediaStore.Images.Media.DATA}, null, null, null);
//                if (cursor != null) {
//                    cursor.moveToFirst();
//                    photoPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
//                    Log.e("photoPath", photoPath);
//                }
//                cursor.close();
//
//                if (!"".equals(photoPath)) {
//                    //将截取到图片以圆形的方式显示到imglogo
////                    Glide.with(this).load(photoPath).into(photo);
//                    ImageLoaderUtils.displayRound(this, photo, photoPath);
//                    //将截取的头像logo放到缓存中(如果是服务器则需上传到服务器)
//                    ACache.get(AppApplication.getAppContext()).put(AppConstant.STORE_PERSON_PHOTO, photoPath);
//                }
//            }
//        }

        //使用的图库选择器
        if (requestCode == REQUEST_PHOTO && resultCode == RESULT_OK) {
            List<Uri> uris = Matisse.obtainResult(data);
            ImageLoaderUtils.displayRound(this, photo, uris.get(uris.size()-1).toString());
        }

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        int id = item.getItemId();
        switch (id) {
            case  R.id.nav_menu_news:
                fab.show();
                toolbar.setTitle("设置");
                transaction.show(newsMainFragment);
                transaction.hide(aboutFragment);
                break;
            case  R.id.nav_menu_picture:
                fab.hide();
                toolbar.setTitle("搜索");
                transaction.hide(newsMainFragment);
                transaction.hide(aboutFragment);
                break;
            case R.id.nav_menu_station:
                fab.hide();
                toolbar.setTitle("地区");
                transaction.hide(newsMainFragment);
                transaction.hide(aboutFragment);
                break;
            case R.id.nav_menu_favorite:
                fab.hide();
                toolbar.setTitle("收藏");
                transaction.hide(newsMainFragment);
                transaction.hide(aboutFragment);
                break;
            case R.id.nav_menu_setting:
                fab.hide();
                toolbar.setTitle("设置");
                transaction.hide(newsMainFragment);
                transaction.hide(aboutFragment);
                break;
            case R.id.nav_menu_about:
                fab.hide();
                toolbar.setTitle("关于");
                transaction.show(aboutFragment);
                transaction.hide(newsMainFragment);
                break;
            default:
                break;
        }
        transaction.commitAllowingStateLoss();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
