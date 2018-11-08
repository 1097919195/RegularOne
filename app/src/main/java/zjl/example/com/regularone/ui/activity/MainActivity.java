package zjl.example.com.regularone.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.RectF;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.baserx.RxBus2;
import com.jaydenxiao.common.commonutils.ACache;
import com.jaydenxiao.common.commonutils.ImageLoaderUtils;
import com.jaydenxiao.common.commonutils.ToastUtil;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.List;

import butterknife.BindView;
import util.UpdateAppUtils;
import zhy.com.highlight.HighLight;
import zhy.com.highlight.interfaces.HighLightInterface;
import zhy.com.highlight.shape.CircleLightShape;
import zjl.example.com.regularone.BuildConfig;
import zjl.example.com.regularone.R;
import zjl.example.com.regularone.app.AppApplication;
import zjl.example.com.regularone.app.AppConstant;
import zjl.example.com.regularone.ui.fragment.AboutFragment;
import zjl.example.com.regularone.ui.fragment.MineFragment;
import zjl.example.com.regularone.ui.fragment.NewsMainFragment;
import zjl.example.com.regularone.widget.BottomNavigationViewEx;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.bottom_nav)
    BottomNavigationViewEx bottomNavigationView;
    private static final int REQUEST_CODE = 1000;
    private static final int REQUEST_PHOTO = 1001;
    NewsMainFragment newsMainFragment;
    AboutFragment aboutFragment;
    MineFragment mineFragment;

    ImageView photo;
    private HighLight mHightLight;

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
        toolbar.setTitle("图片");

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
        navigationView.setCheckedItem(R.id.nav_menu_picture);
        initPersonPhoto();
        initListener();
        initHighLight();
        initBottomNavigation();
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
                                titleResId = R.string.search;
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
        switch (position) {
            case R.id.nav_menu_picture:
            case 0:
                fab.show();
                toolbar.setTitle("图片");
                transaction.show(newsMainFragment);
                transaction.hide(aboutFragment);
                transaction.hide(mineFragment);
                break;
            case R.id.nav_menu_search:
            case 1:
                fab.hide();
                toolbar.setTitle("搜索");
                transaction.hide(newsMainFragment);
                transaction.hide(aboutFragment);
                transaction.hide(mineFragment);
                break;
            case R.id.nav_menu_station:
            case 2:
                fab.hide();
                toolbar.setTitle("地区");
                transaction.hide(newsMainFragment);
                transaction.hide(aboutFragment);
                transaction.hide(mineFragment);
                break;
            case R.id.nav_menu_favorite:
            case 3:
                fab.hide();
                toolbar.setTitle("收藏");
                transaction.hide(newsMainFragment);
                transaction.hide(aboutFragment);
                transaction.show(mineFragment);
                break;
            case R.id.nav_menu_setting:
                fab.hide();
                toolbar.setTitle("设置");
                transaction.hide(newsMainFragment);
                transaction.hide(aboutFragment);
                transaction.hide(mineFragment);
                break;
            case R.id.nav_menu_about:
                fab.hide();
                toolbar.setTitle("关于");
                transaction.show(aboutFragment);
                transaction.hide(newsMainFragment);
                transaction.hide(mineFragment);
                break;
            default:
                break;
        }
        transaction.commitAllowingStateLoss();
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
        aboutFragment = new AboutFragment();
        mineFragment = new MineFragment();
        transaction.add(R.id.rl_body, newsMainFragment, "newsMainFragment");
        transaction.add(R.id.rl_body, aboutFragment, "aboutFragment");
        transaction.add(R.id.rl_body, mineFragment, "mineFragment");
        transaction.hide(aboutFragment);
        transaction.hide(mineFragment);
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
        mLoadingDialog = new Dialog(this, R.style.CustomProgressDialog);
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
            ImageLoaderUtils.displayRound(this, photo, uris.get(uris.size() - 1).toString());
        }

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

}
