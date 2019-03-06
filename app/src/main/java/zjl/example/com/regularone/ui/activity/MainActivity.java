package zjl.example.com.regularone.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.baseapp.AppManager;
import com.jaydenxiao.common.commonutils.ACache;
import com.jaydenxiao.common.commonutils.ImageLoaderUtils;
import com.jaydenxiao.common.commonutils.LogUtils;
import com.jaydenxiao.common.commonutils.ToastUtil;
import com.tencent.bugly.beta.Beta;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import cn.jzvd.Jzvd;
import io.reactivex.functions.Consumer;
import util.UpdateAppUtils;
import zhy.com.highlight.HighLight;
import zhy.com.highlight.interfaces.HighLightInterface;
import zhy.com.highlight.shape.CircleLightShape;
import zjl.example.com.regularone.BuildConfig;
import zjl.example.com.regularone.R;
import zjl.example.com.regularone.app.AppApplication;
import zjl.example.com.regularone.app.AppConstant;
import zjl.example.com.regularone.bean.WeatherInfo;
import zjl.example.com.regularone.ui.activity.test.TestActivity;
import zjl.example.com.regularone.ui.fragment.AboutFragment;
import zjl.example.com.regularone.ui.fragment.MineFragment;
import zjl.example.com.regularone.ui.fragment.NewsMainFragment;
import zjl.example.com.regularone.ui.fragment.NavigationFragment;
import zjl.example.com.regularone.ui.fragment.PreferenceSettingFragment;
import zjl.example.com.regularone.ui.fragment.VideoFragment;
import zjl.example.com.regularone.ui.main.contract.MainContract;
import zjl.example.com.regularone.ui.main.module.MainModule;
import zjl.example.com.regularone.ui.main.presenter.MainPresenter;
import zjl.example.com.regularone.utils.TipsToast;
import zjl.example.com.regularone.widget.BottomNavigationViewEx;


public class MainActivity extends BaseActivity<MainPresenter,MainModule> implements MainContract.View, NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.ivLoadView)
    ImageView ivLoadView;
    @BindView(R.id.locationNow)
    TextView locationNow;
    @BindView(R.id.weather_condition)
    TextView weather_condition;
    @BindView(R.id.temperature)
    TextView temperature;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.bottom_nav)
    BottomNavigationViewEx bottomNavigationView;
    private static final int REQUEST_CODE = 1000;
    private static final int REQUEST_PHOTO = 1001;
    private static final int CROP_PICTURE = 2000;
    NewsMainFragment newsMainFragment;
    VideoFragment videoFragment;
    AboutFragment aboutFragment;
    MineFragment mineFragment;
    NavigationFragment navigationFragment;
    PreferenceSettingFragment settingFragment;

    ImageView photo;
    private HighLight mHightLight;
    private TipsToast tipsToast;
    private long exitTime = 0;

    @Override
    public int getLayoutId() {
        return R.layout.act_main;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this,mModel);
    }

    @Override
    public void initView() {
        ImmersionBar.with(this)
                .statusBarColor(R.color.main_color).init();//保证使用同一种statusbar工具，防止透明状态栏和着色状态栏之间切换布局移动的问题，因为baseActivity中有另外一种

        Beta.checkUpgrade(false,false);//检查更新
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.picture);

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
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                mPresenter.getWeatherInfoRequest();//打开了就要更新天气的状态
            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

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
//        navigationView.setCheckedItem(R.id.nav_menu_picture);//设置菜单默认选中项(暂时取消了选中的功能)
        initPersonPhoto();
        initListener();
        initHighLight();//第一次的高亮引导
        initBottomNavigation();
        initRxBusListener();
    }

    private void initRxBusListener() {
        mRxManager.on(AppConstant.MENU_SHOW_HIDE, new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    bottomNavigationView.setVisibility(View.VISIBLE);
                }else {
                    bottomNavigationView.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initBottomNavigation() {
        bottomNavigationView.enableAnimation(false);
        bottomNavigationView.enableShiftingMode(false);
        bottomNavigationView.enableItemShiftingMode(false);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        int titleResId = R.string.picture;
                        switch (item.getItemId()) {
                            case R.id.picture:
                                selectFragment(0);
                                titleResId = R.string.picture;
                                break;
                            case R.id.search:
                                selectFragment(1);
                                titleResId = R.string.video;
                                break;
                            case R.id.navigation:
                                selectFragment(2);
                                titleResId = R.string.navigation;
                                break;
                            case R.id.mine:
                                selectFragment(3);
                                titleResId = R.string.mine;
                                break;
                        }
                        getSupportActionBar().setTitle(titleResId);
                        return true;
                    }
                });
    }

    private void selectFragment(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        android.app.FragmentTransaction transactionWithPre = getFragmentManager().beginTransaction();//注意PreferenceFragment使用的包不同

        if (position != R.id.nav_menu_setting) {
            ivLoadView.setVisibility(View.VISIBLE);
        }
        switch (position) {
            //主页兼侧滑的图片分类
//            case R.id.nav_menu_read:
            case 0:
                fab.show();
                transaction.show(newsMainFragment);
                transaction.hide(aboutFragment);
                transaction.hide(mineFragment);
                transaction.hide(navigationFragment);
                transactionWithPre.hide(settingFragment);
                transaction.hide(videoFragment);
                break;
                //视频专区
            case 1:
                fab.hide();
                transaction.hide(newsMainFragment);
                transaction.hide(aboutFragment);
                transaction.hide(mineFragment);
                transaction.hide(navigationFragment);
                transactionWithPre.hide(settingFragment);
                transaction.show(videoFragment);
                break;
                //文档分类
            case 2:
                fab.hide();
                transaction.hide(newsMainFragment);
                transaction.hide(aboutFragment);
                transaction.hide(mineFragment);
                transaction.show(navigationFragment);
                transactionWithPre.hide(settingFragment);
                transaction.hide(videoFragment);
                break;
                //我的
            case 3:
                fab.hide();
                transaction.hide(newsMainFragment);
                transaction.hide(aboutFragment);
                transaction.show(mineFragment);
                transaction.hide(navigationFragment);
                transactionWithPre.hide(settingFragment);
                transaction.hide(videoFragment);
                break;
            case R.id.nav_menu_read:
                Intent read = new Intent(this, ReadNewsActivity.class);
                startActivity(read);
                break;
            case R.id.nav_menu_search:
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_menu_station://这块内容到时候也可以切换为图标
                Intent media = new Intent(this, TestActivity.class);
                startActivity(media);
                break;
            case R.id.nav_menu_favorite:
//                fab.hide();
//                toolbar.setTitle("收藏");
//                transaction.hide(newsMainFragment);
//                transaction.hide(aboutFragment);
//                transaction.hide(mineFragment);
//                transaction.hide(navigationFragment);
//                transactionWithPre.hide(settingFragment);
                Intent favorite = new Intent(this, FavoriteActivity.class);
                startActivity(favorite);
                break;
            case R.id.nav_menu_setting:
                fab.hide();
                toolbar.setTitle("设置");
                transaction.hide(newsMainFragment);
                transaction.hide(aboutFragment);
                transaction.hide(mineFragment);
                transaction.hide(navigationFragment);
                transactionWithPre.show(settingFragment);
                transaction.hide(videoFragment);
                // TODO: 2018/12/17 0017
                // 这个偏好fragment好像不会覆盖，所以需要把这里的加载动画隐藏（这里有空最好实现一下整体的封装，这里是简单的添加在了MainActivity中）
                ivLoadView.setVisibility(View.GONE);
                break;
            case R.id.nav_menu_about:
                fab.hide();
                toolbar.setTitle("关于");
                transaction.hide(newsMainFragment);
                transaction.show(aboutFragment);
                transaction.hide(mineFragment);
                transaction.hide(navigationFragment);
                transactionWithPre.hide(settingFragment);
                transaction.hide(videoFragment);
                break;
            default:
                break;
        }
        transaction.commitAllowingStateLoss();
        transactionWithPre.commitAllowingStateLoss();
    }

    private void initHighLight() {
        mHightLight = new HighLight(MainActivity.this)//
                .anchor(findViewById(R.id.drawer_layout))//如果是Activity上增加引导层，不需要设置anchor
                .autoRemove(false)//设置背景点击高亮布局自动移除为false 默认为true
                .intercept(true)//拦截属性默认为true 使下方ClickCallback生效
//                .enableNext()
                .setOnLayoutCallback(new HighLightInterface.OnLayoutCallback() {
                    @Override
                    public void onLayouted() {
                        //界面布局完成添加tipview
                        mHightLight.addHighLight(R.id.fab, R.layout.info_known, new HighLight.OnPosCallback() {
                            /**
                             * @param rightMargin 高亮view在anchor中的右边距
                             * @param bottomMargin 高亮view在anchor中的下边距
                             * @param rectF 高亮view的l,t,r,b,w,h都有
                             * @param marginInfo 设置你的布局的位置，一般设置l,t或者r,b
                             */
                            @Override
                            public void getPos(float rightMargin, float bottomMargin, RectF rectF, HighLight.MarginInfo marginInfo) {
                                marginInfo.rightMargin = rightMargin + rectF.width() / 2;
                                marginInfo.bottomMargin = bottomMargin + rectF.height();
                            }
                        }, new CircleLightShape());
                        //然后显示高亮布局
                        mHightLight.show();
                    }
                });
    }

    /**
     * 响应所有R.id.iv_known的控件的点击事件
     * <p>
     * 移除高亮布局
     * </p>
     * @param view
     */
    public void clickKnown(View view) {
        if (mHightLight.isShowing() && mHightLight.isNext())//如果开启next模式
        {
            mHightLight.next();
        } else {
            mHightLight.remove();
        }
    }

    private void initPersonPhoto() {
        //根据有无缓存 加载显示头像(如果是服务器则需要从服务器获取)
        if (ACache.get(AppApplication.getAppContext()).getAsString(AppConstant.STORE_PERSON_PHOTO) == null) {
            ImageLoaderUtils.displayRound(this, photo, R.drawable.bg_about_port);
        } else {
            ImageLoaderUtils.displayRound(this, photo, ACache.get(AppApplication.getAppContext()).getAsString(AppConstant.STORE_PERSON_PHOTO));
        }
    }

    private void initListener() {
        photo.setOnClickListener(v -> {
            //直接调用系统的打开相册
//            openPicture();
            //使用的图库选择器
            Matisse.from(MainActivity.this)
                    .choose(MimeType.allOf()) // 选择 mime 的类型
                    .countable(true)
                    .capture(true)  // 开启相机，和 captureStrategy 一并使用否则报错
                    .captureStrategy(new CaptureStrategy(false, BuildConfig.APPLICATION_ID + ".MyFileProvider")) // 第一个参数表示是否是私有存储位置
                    .maxSelectable(9) // 图片选择的最多数量
                    .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                    .theme(R.style.Matisse_Zhihu)
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
        videoFragment = new VideoFragment();
        aboutFragment = new AboutFragment();
        mineFragment = new MineFragment();
        navigationFragment = new NavigationFragment();
        settingFragment = new PreferenceSettingFragment();
        transaction.add(R.id.rl_body, newsMainFragment, "newsMainFragment");
        transaction.add(R.id.rl_body, videoFragment, "videoFragment");
        transaction.add(R.id.rl_body, aboutFragment, "aboutFragment");
        transaction.add(R.id.rl_body, mineFragment, "mineFragment");
        transaction.add(R.id.rl_body, navigationFragment, "navigationFragment");
        transaction.hide(videoFragment);
        transaction.hide(aboutFragment);
        transaction.hide(mineFragment);
        transaction.hide(navigationFragment);
        transaction.commit();

        android.app.FragmentTransaction transactionWithPre = getFragmentManager().beginTransaction();
        transactionWithPre.add(R.id.rl_body, settingFragment, "settingFragment");
        transactionWithPre.hide(settingFragment);
        transactionWithPre.commit();
    }

    private void loadingViewTest() {
        AnimationDrawable animationDrawable = (AnimationDrawable) ivLoadView.getDrawable();
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
        mLoadingDialog = new Dialog(this, R.style.CustomProgressDialog);
        mLoadingDialog.setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        mLoadingDialog.show();
    }

    @Override
    public void returnWeatherInfo(WeatherInfo weatherInfo) {
        if (weatherInfo != null) {
            locationNow.setText(weatherInfo.getResults().get(0).getLocation().getName());
            weather_condition.setText(weatherInfo.getResults().get(0).getNow().getText());
            temperature.setText(weatherInfo.getResults().get(0).getNow().getTemperature() + "°");

            String s = new Gson().toJson(weatherInfo);
            LogUtils.loge(s);
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

    //该事件会被onKeyDown先执行（所以关闭侧滑事件和判断双击退出需要在onkeyDown里面判断）
    @Override
    public void onBackPressed() {
        LogUtils.loge("Main--onBackPressed");
//        if (Jzvd.backPress()) {//因为添加了onKeyDown事件，所以此处无效，添加到onKeyDown事件中判断
//            return;
//        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
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
            String photoPath = uris.get(uris.size() - 1).toString();
            ImageLoaderUtils.displayRound(this, photo, photoPath);
            //将截取的头像logo放到缓存中(如果是服务器则需上传到服务器)
            ACache.get(AppApplication.getAppContext()).put(AppConstant.STORE_PERSON_PHOTO, photoPath);

//            cropImageUri(uris.get(uris.size() - 1), 300, 300, CROP_PICTURE);//简单的调用裁剪(待优化)
        }

        //裁剪选择的图片
        if (requestCode == CROP_PICTURE) {
            if(data != null){
                Bitmap bitmap = data.getParcelableExtra("data");
                photo.setImageBitmap(bitmap);
            }else{
                LogUtils.loge("CHOOSE_SMALL_PICTURE: data = " + data);
            }
        }
    }

    private void cropImageUri(Uri uri, int outputX, int outputY, int requestCode){
        File file = new File(uri + "/" + System.currentTimeMillis() + ".jpg");
        LogUtils.loge(file.toString());

        Intent intent = new Intent("com.android.camera.action.CROP");

        intent.setDataAndType(uri, "image/*");

        intent.putExtra("crop", "true");

        intent.putExtra("aspectX", 1);

        intent.putExtra("aspectY", 1);

        intent.putExtra("outputX", outputX);

        intent.putExtra("outputY", outputY);

        intent.putExtra("scale", true);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));

        intent.putExtra("return-data", true);//这里有个参数"return-data" 是否返回数据。建议，裁剪高分辨率图片，使用Uri不返回数据，小图使用Bitmap并返回数据。

        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        intent.putExtra("noFaceDetection", true); // no face detection

        startActivityForResult(intent, requestCode);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        selectFragment(id);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Jzvd.backPress()) {
            return false;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //双击而下才能推出
            if (keyCode == KeyEvent.KEYCODE_BACK
                    && event.getAction() == KeyEvent.ACTION_DOWN) {
                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    showTips(R.drawable.tips_smile, "再按一次退出程序");
//                ToastUtil.showToastWithImg("再按一次退出程序", R.drawable.bg_about);
                    exitTime = System.currentTimeMillis();
                } else {
                    if (tipsToast != null) {
                        tipsToast.cancel();
                    }
                    AppManager.getAppManager().finishAllActivity();
                    finish();
                }
                return true;
            }

            //不退出程序返回到桌面
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            Intent home = new Intent(Intent.ACTION_MAIN);
//            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            home.addCategory(Intent.CATEGORY_HOME);
//            startActivity(home);
//            return true;
//        }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 自定义toast
     * @param iconResId 图片
     * @param tips      提示文字
     */
    private void showTips(int iconResId, String tips) {
        if (tipsToast == null) {
            tipsToast = TipsToast.makeText(AppApplication.getAppContext(), tips, Toast.LENGTH_SHORT);
        }
        tipsToast.setIcon(iconResId);
        tipsToast.setText(tips);
        tipsToast.show();
    }
}
