package zjl.example.com.regularone.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jaydenxiao.common.base.BaseFragment;
import com.jaydenxiao.common.commonutils.LogUtils;
import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import zjl.example.com.regularone.R;
import zjl.example.com.regularone.bean.thirdPartyBean.UserInfo_QQ;
import zjl.example.com.regularone.ui.activity.LoginActivity;
import zjl.example.com.regularone.ui.activity.MainActivity;
import zjl.example.com.regularone.ui.mine.activity.LogisticsActivity;
import zjl.example.com.regularone.ui.mine.activity.StatisticsActivity;
import zjl.example.com.regularone.utils.BaseUIListener;
import zjl.example.com.regularone.utils.Util;

/**
 * Created by Administrator on 2018/11/8 0008.
 */

public class MineFragment extends BaseFragment {
    @BindView(R.id.login_card)
    CardView login_card;
    @BindView(R.id.logistics_item)
    LinearLayout logistics_item;
    @BindView(R.id.statistics_item)
    LinearLayout statistics_item;
    @BindView(R.id.avatar)
    CircleImageView avatar;

    @BindView(R.id.loginOrRegister)
    TextView loginOrRegister;
    @BindView(R.id.logistics_tv)
    TextView logistics_tv;
    @BindView(R.id.statistics_tv)
    TextView statistics_tv;

    Typeface typeface;
    String rmsg;
    UserInfo_QQ info_qq;
    @Override
    protected int getLayoutResource() {
        return R.layout.frag_mine;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {
        typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/fzxk.ttf");
        loginOrRegister.setTypeface(typeface);
        logistics_tv.setTypeface(typeface);
        statistics_tv.setTypeface(typeface);
        initListener();
    }

    private void initListener() {
        login_card.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        });

        logistics_item.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LogisticsActivity.class);
            startActivity(intent);
        });

        statistics_item.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), StatisticsActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (LoginActivity.mTencent != null) {
            if (LoginActivity.mTencent.isSessionValid()) {
                updataUserInfo();
            }else {
                avatar.setImageResource(R.drawable.ic_menu_mine);
//                avatar.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ic_menu_mine));
            }
        }

    }

    //QQ登录后调用方法更新信息
    public void updataUserInfo() {
        if (LoginActivity.ready(getActivity())) {
            LoginActivity.mInfo.getUserInfo(new IUiListener() {
                @Override
                public void onComplete(Object o) {
                    if (o != null) {
                        JSONObject response = (JSONObject)o;
                        rmsg = response.toString();//这里获取到的URL是转义字符，直接使用的话需要 .replaceAll("\\\\","")
                        LogUtils.loge(response.toString());
//                        info_qq=new Gson().fromJson(rmsg, new TypeToken<UserInfo_QQ>() {}.getType());//json转化成实体类，可参考发报项目中的JSONArray转化为实体（可用于泛型信息）
                        info_qq = new Gson().fromJson(rmsg, UserInfo_QQ.class);//直接转化
                        LogUtils.loge(new Gson().toJson(info_qq));
                        Glide.with(getActivity())
                                .load(info_qq.getFigureurl_2())//这里获取到的URL有转义字符
                                .into(avatar);
                    }
                }

                @Override
                public void onError(UiError uiError) {

                }

                @Override
                public void onCancel() {

                }
            });
        }
    }

}
