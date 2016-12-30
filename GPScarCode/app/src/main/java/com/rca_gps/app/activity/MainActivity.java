package com.rca_gps.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.rca_gps.app.R;
import com.rca_gps.app.application.MyApplication;
import com.rca_gps.app.model.UserInfo;
import com.rca_gps.app.util.ShareP;
import com.rca_gps.app.util.StringUtil;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendAuth;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends BaseActivity {
    private String APP_ID = "wx3a81e0351d9e6bc7";
    //5860c28c9086ca0da3abf72bfe231e96
    public static final String APP_SECRET = "f2c853f25b0e9da10e03bf1b403a3208";
    private IWXAPI api;
    private SendAuth.Req req;
    private static final String WEIXIN_SCOPE = "snsapi_userinfo";
    private static final String WEIXIN_STATE = "login_state";
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                String wxToken = (String) msg.obj;
                getAccess_token(wxToken);
            }
        }
    };

    @Override
    public void initViews() {
        super.initViews();
        setContentView(R.layout.activity_main);
        MyApplication.getInstance().setWxHandler(handler);
        api = WXAPIFactory.createWXAPI(this, APP_ID);
        api.registerApp(APP_ID);

        if (StringUtil.isNotBlank(ShareP.getUserId(context))) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, UserInfoActivity.class);
            startActivity(intent);
            finish();
        }
        findViewById(R.id.iv_back).setVisibility(View.INVISIBLE);
        findViewById(R.id.layout_wx).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAuth();
            }
        });
    }

    private void sendAuth() {
        req = new SendAuth.Req();
        req.scope = WEIXIN_SCOPE;
        req.state = WEIXIN_STATE;
        api.sendReq(req);
    }

    @Override
    public void success(String result, String type) {
        super.success(result, type);
        if (type.equals("access_token")) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                String openid = jsonObject.getString("openid").toString().trim();
                String access_token = jsonObject.getString("access_token").toString().trim();
                getUserMesg(access_token, openid);
            } catch (JSONException e) {
                e.printStackTrace();
                senToa(e.getMessage());
            }
        } else if (type.equals("userMsg")) {
            UserInfo userInfo = JSON.parseObject(result.toString(), UserInfo.class);
            ShareP.saveUserId(context, userInfo.getUnionid());
            ShareP.saveUserName(context, userInfo.getNickname());
            ShareP.saveUserPic(context, userInfo.getHeadimgurl());
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("userInfo", userInfo);
            intent.putExtras(bundle);
            intent.setClass(MainActivity.this, UserInfoActivity.class);
            startActivity(intent);
            finish();
        }
    }

    /**
     * 获取openid accessToken值用于后期操作
     *
     * @param code 请求码
     */
    private void getAccess_token(final String code) {
        String path = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
                + APP_ID
                + "&secret="
                + APP_SECRET
                + "&code="
                + code
                + "&grant_type=authorization_code";
        http(path, "access_token");
    }


    /**
     * 获取微信的个人信息
     *
     * @param access_token
     * @param openid
     */
    private void getUserMesg(final String access_token, final String openid) {
        String path = "https://api.weixin.qq.com/sns/userinfo?access_token="
                + access_token
                + "&openid="
                + openid;
        System.out.println("url:" + path);
        http(path, "userMsg");
    }
}
