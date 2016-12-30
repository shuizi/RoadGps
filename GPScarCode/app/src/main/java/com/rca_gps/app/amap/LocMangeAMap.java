package com.rca_gps.app.amap;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;


/**
 * Created by wafer on 2016/9/2.
 */
public class LocMangeAMap {
    public final static String LOG_TAG = "locationMap";

    public interface GotLocCallBackShort {
        void onGotLocShort(double lat, double lng, AMapLocation amapLocation);

        void onGotLocShortFailed(String location);
    }

    private GotLocCallBackShort mGotLocCallBack;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    private AMapLocationClientOption mLocationOption;

    public LocMangeAMap(Context context) {
        mLocationClient = new AMapLocationClient(context);
    }

    public void getLocation(GotLocCallBackShort gotLocCallBack) {
        mGotLocCallBack = gotLocCallBack;
        //设置定位回调监听
        mLocationClient.setLocationListener(locationListener);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(5000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    if (mGotLocCallBack != null) {
                        mGotLocCallBack.onGotLocShort(amapLocation.getLatitude(), amapLocation.getLongitude(), amapLocation);
                    }
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                    mGotLocCallBack.onGotLocShortFailed(null);
                }
            }
        }
    };

    public void stopLocation() {
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
        }
    }

    public void updatelocation(GotLocCallBackShort gotLocCallBack) {
        if (mLocationClient != null)
            mLocationClient.stopLocation();
        mGotLocCallBack = gotLocCallBack;
        //设置定位回调监听
        mLocationClient.setLocationListener(locationListener);
//        //初始化定位参数
//        mLocationOption = new AMapLocationClientOption();
//        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//        //设置是否返回地址信息（默认返回地址信息）
//        mLocationOption.setNeedAddress(true);
//        //设置是否只定位一次,默认为false
//        mLocationOption.setOnceLocation(false);
//        //设置是否强制刷新WIFI，默认为强制刷新
//        //设置是否允许模拟位置,默认为false，不允许模拟位置
//        mLocationOption.setMockEnable(false);
//        mLocationOption.setWifiActiveScan(true);
//        //设置定位间隔,单位毫秒,默认为2000ms
//        mLocationOption.setInterval(600000);
//        //给定位客户端对象设置定位参数
//        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }
}
