package com.daizhihao.mobilesafe.service;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * 获取经纬度坐标的service
 * Created by Administrator on 2016/4/30.
 */
public class LocationService extends Service {

    private SharedPreferences mPref;
    private LocationManager lm;
    private MyLocationListener listener;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onCreate() {
        super.onCreate();
        mPref = getSharedPreferences("config", MODE_PRIVATE);
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setCostAllowed(true);// 是否允许付费,比如使用网络定位
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String bestProvider = lm.getBestProvider(criteria, true);// 获取最佳位置提供者
        listener = new MyLocationListener();
        lm.requestLocationUpdates(bestProvider, 0, 0, listener);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class MyLocationListener implements LocationListener {
        //位置发生改变调用该方法
        @Override
        public void onLocationChanged(Location location) {
            // 将获取的经纬度保存在sp中
            mPref.edit()
                    .putString(
                            "location",
                            "j:" + location.getLongitude() + "; w:"
                                    + location.getLatitude()).commit();
            stopSelf();//停掉service
        }
        //位置提供者状态发生变化
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
        //开启手机定义服务
        @Override
        public void onProviderEnabled(String provider) {

        }

        //关闭手机定位服务
        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onDestroy() {
        super.onDestroy();
        lm.removeUpdates(listener);// 当activity销毁时,停止更新位置, 节省电量
    }
}
