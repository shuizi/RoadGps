package com.rca_gps.app.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.rca_gps.app.util.CheckNetwork;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;


/**
 * Created by wafer on 2016/10/24.
 */

public class BaseActivity extends Activity {
    public Context context;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        initViews();
    }

    public void initViews() {
        context = this;
    }

    public void http(String url, final String type) {
        if (CheckNetwork.isNetworkAvailable(BaseActivity.this)) {
            showDia("正在加载中...");
            RequestParams params = new RequestParams(url);
            x.http().get(params, new Callback.CacheCallback<String>() {
                @Override
                public boolean onCache(String result) {
                    return false;
                }

                @Override
                public void onSuccess(String result) {
                    success(result, type);
                    hideDia();
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    error(ex.getMessage(), type);
                    System.out.println(ex.getLocalizedMessage());
                    hideDia();
                }

                @Override
                public void onCancelled(CancelledException cex) {
//                    hideDia();
                }

                @Override
                public void onFinished() {
//                    hideDia();
                }
            });
        } else {
            senToa("网络异常");
        }

    }

    public void httpPost(final String type, RequestParams params) {
        if (CheckNetwork.isNetworkAvailable(BaseActivity.this)) {
            if (!type.equals("commit"))
                showDia("正在加载中...");
            x.http().post(params, new Callback.CacheCallback<String>() {
                @Override
                public boolean onCache(String result) {
                    return false;
                }

                @Override
                public void onSuccess(String result) {
                    success(result, type);
                    if (!type.equals("commit"))
                        hideDia();
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    error(ex.getMessage(), type);
                    if (!type.equals("commit"))
                        hideDia();
                }

                @Override
                public void onCancelled(CancelledException cex) {
//                    hideDia();
                }

                @Override
                public void onFinished() {
//                    hideDia();
                }
            });
        } else {
            senToa("网络异常");
        }

    }

    public void success(String result, String type) {

    }

    public void error(String result, String type) {

    }

    public void senToa(String text) {
        Toast mToast = null;
        if (mToast != null) {
            mToast = new Toast(context);
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_LONG);
            mToast.show();
        }
    }

    private void showDia(String str) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }
        progressDialog.setMessage(str);
        progressDialog.show();
    }

    private void hideDia() {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
