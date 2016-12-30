package com.rca_gps.app.application;

import android.app.Application;
import android.os.Handler;

import com.rca_gps.app.activity.MainActivity;
import com.rca_gps.app.amap.LocMangeAMap;
import com.rca_gps.app.util.ShareP;
import com.rca_gps.app.util.StringUtil;

import org.xutils.x;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by wafer on 2016/10/20.
 */

public class MyApplication extends Application {
    private static MyApplication application;
    private Handler wxHandler;

    public static MyApplication getInstance() {
        if (application == null) {
            application = new MyApplication();
        }
        return application;
    }

    private static LocMangeAMap locMangeAMap;

    public static LocMangeAMap getLocMangeAMap() {
        return locMangeAMap;
    }

    public static void setLocMangeAMap(LocMangeAMap locMangeAMap) {
        MyApplication.locMangeAMap = locMangeAMap;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        String id = JPushInterface.getRegistrationID(getApplicationContext());
        if (StringUtil.isBlank(ShareP.getJpshId(getApplicationContext())) && StringUtil.isNotBlank(id))
            ShareP.saveJpshId(getApplicationContext(), id);
        initAmap();
    }

    private void initAmap() {
        locMangeAMap = new LocMangeAMap(getApplicationContext());
    }

    public Handler getWxHandler() {
        return wxHandler;
    }

    public void setWxHandler(Handler wxHandler) {
        this.wxHandler = wxHandler;
    }
}
