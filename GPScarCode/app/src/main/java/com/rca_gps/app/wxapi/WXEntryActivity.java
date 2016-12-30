package com.rca_gps.app.wxapi;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.rca_gps.app.activity.BaseActivity;
import com.rca_gps.app.activity.UserInfoActivity;
import com.rca_gps.app.application.MyApplication;
import com.rca_gps.app.model.UserInfo;
import com.rca_gps.app.util.ShareP;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.SendAuth;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private String APP_ID = "wx3a81e0351d9e6bc7";
    public static final String APP_SECRET = "f2c853f25b0e9da10e03bf1b403a3208";
    private IWXAPI api;
    private ProgressDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, "wx3a81e0351d9e6bc7", false);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp resp) {
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                SendAuth.Resp sendResp = (SendAuth.Resp) resp;
                String token = sendResp.token;
                Message message = new Message();
                message.obj = token;
                message.what = 1;
                MyApplication.getInstance().getWxHandler().sendMessage(message);
                finish();
                break;
        }
    }
}