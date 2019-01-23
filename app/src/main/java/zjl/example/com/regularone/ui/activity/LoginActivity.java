package zjl.example.com.regularone.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.commonutils.ToastUtil;
import com.tencent.connect.UnionInfo;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import butterknife.BindView;
import zjl.example.com.regularone.R;
import zjl.example.com.regularone.ui.fragment.MineFragment;
import zjl.example.com.regularone.utils.BaseUIListener;
import zjl.example.com.regularone.utils.EditTextInputHelper;
import zjl.example.com.regularone.utils.Util;

/**
 * Created by Administrator on 2018/10/30 0030.
 */

public class LoginActivity extends BaseActivity {
    @BindView(R.id.link_signup)
    TextView link_signup;
    @BindView(R.id.sign_in_button)
    Button sign_in_button;
    @BindView(R.id.username_input)
    TextInputEditText username_input;
    @BindView(R.id.password_input)
    TextInputEditText password_input;
    @BindView(R.id.action_qq_login)
    ImageView action_qq_login;

    private EditTextInputHelper mEditTextInputHelper;

    public static Tencent mTencent;
    private static boolean isServerSideLogin = false;
    public static UserInfo mInfo = null;

    @Override
    public int getLayoutId() {
        return R.layout.act_login;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        //设置登录按钮不可点击
//        sign_in_button.setBackgroundColor(getColor(R.color.gray));
//        sign_in_button.setClickable(false);
        mEditTextInputHelper = new EditTextInputHelper(sign_in_button, false);
        mEditTextInputHelper.addViews(username_input, password_input);
        mTencent = Tencent.createInstance("101543687", this);
        initListener();
    }

    private void initListener() {
        link_signup.setOnClickListener(v -> {

        });

        action_qq_login.setOnClickListener(v -> {
            login_QQ();
        });
    }

    public void Login(View view) {
        finish();
    }

    //供别的界面获取信息
    public static boolean ready(Context context) {
        if (mTencent == null) {
            return false;
        }
        boolean ready = mTencent.isSessionValid()
                && mTencent.getQQToken().getOpenId() != null;
        if (!ready) {
            Toast.makeText(context, "login and get openId first, please!",
                    Toast.LENGTH_SHORT).show();
        }
        return ready;
    }

    public void login_QQ() {
        if (!mTencent.isSessionValid()) {
//            if(!mQrCk.isChecked()){//是否支持扫码
//                mTencent.login(this, "all", loginListener);
//            }else {
//                mTencent.login(this, "all", loginListener,true);
//            }
            mTencent.login(this, "all", loginListener,true);//没有装手Q时支持扫码登录，具体看官方demo使用情况
            isServerSideLogin = false;
            Log.d("SDKQQAgentPref", "FirstLaunch_SDK:" + SystemClock.elapsedRealtime());
            ToastUtil.showShort("QQ登录");
        } else {
            if (isServerSideLogin) { // Server-Side 模式的登陆, 先退出，再进行SSO登陆
                mTencent.logout(this);
                mTencent.login(this, "all", loginListener);
                isServerSideLogin = false;
                Log.d("SDKQQAgentPref", "FirstLaunch_SDK:" + SystemClock.elapsedRealtime());
                return;
            }
            mTencent.logout(this);
            ToastUtil.showShort("QQ登出");
//            updateUserInfo();
//            updateLoginButton();
        }
    }

    IUiListener loginListener = new BaseUiListener() {
        @Override
        protected void doComplete(JSONObject values) {
            Log.d("SDKQQAgentPref", "AuthorSwitch_SDK:" + SystemClock.elapsedRealtime());
            initOpenidAndToken(values);
//            updateUserInfo();
//            updateLoginButton();

            //具体APP需要实现的UI更新
            mInfo = new UserInfo(LoginActivity.this, LoginActivity.mTencent.getQQToken());
//            if (LoginActivity.ready(LoginActivity.this)) {//打印用户信息和加载框
//                mInfo.getUserInfo(new BaseUIListener(LoginActivity.this,"get_simple_userinfo"));
//                Util.showProgressDialog(LoginActivity.this, null, null);
//            }
            finish();
        }
    };

    public static void initOpenidAndToken(JSONObject jsonObject) {
        try {
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(openId)) {
                mTencent.setAccessToken(token, expires);
                mTencent.setOpenId(openId);
            }
        } catch(Exception e) {
        }
    }

    //我们线上已经有两个应用，由于每个应用的openid不一样，所以不能将不同应用的用户统一起来，后来知道有一个unionID是唯一的，所以我们自然想到用unionID来统一用户
    private void getUnionId() {
        if (mTencent != null && mTencent.isSessionValid()) {
            IUiListener listener = new IUiListener() {
                @Override
                public void onError(UiError e) {
                    Toast.makeText(LoginActivity.this,"onError",Toast.LENGTH_LONG).show();
                }

                @Override
                public void onComplete(final Object response) {
                    if(response != null){
                        JSONObject jsonObject = (JSONObject)response;
                        try {
                            String unionid = jsonObject.getString("unionid");
                            Util.showResultDialog(LoginActivity.this, "unionid:\n"+unionid, "onComplete");
                            Util.dismissDialog();
                        }catch (Exception e){
                            Toast.makeText(LoginActivity.this,"no unionid",Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(LoginActivity.this,"no unionid",Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancel() {
                    Toast.makeText(LoginActivity.this,"onCancel",Toast.LENGTH_LONG).show();
                }
            };
            UnionInfo unionInfo = new UnionInfo(this, mTencent.getQQToken());
            unionInfo.getUnionId(listener);
        } else {
            Toast.makeText(this,"please login frist!",Toast.LENGTH_LONG).show();
        }
    }

    private class BaseUiListener implements IUiListener {
        @Override
        public void onComplete(Object response) {
            if (null == response) {
                Util.showResultDialog(LoginActivity.this, "返回为空", "登录失败");
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (null != jsonResponse && jsonResponse.length() == 0) {
                Util.showResultDialog(LoginActivity.this, "返回为空", "登录失败");
                return;
            }
//            Util.showResultDialog(LoginActivity.this, response.toString(), "登录成功");//实用时不需要弹出，测试时弹出就可以了
            // 有奖分享处理
//            handlePrizeShare();
            doComplete((JSONObject)response);
        }

        protected void doComplete(JSONObject values) {

        }

        @Override
        public void onError(UiError e) {
            Util.toastMessage(LoginActivity.this, "onError: " + e.errorDetail);
            Util.dismissDialog();
        }

        @Override
        public void onCancel() {
            Util.toastMessage(LoginActivity.this, "onCancel: ");
            Util.dismissDialog();
            if (isServerSideLogin) {
                isServerSideLogin = false;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN ||
                requestCode == Constants.REQUEST_APPBAR) {
            Tencent.onActivityResultData(requestCode,resultCode,data,loginListener);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
