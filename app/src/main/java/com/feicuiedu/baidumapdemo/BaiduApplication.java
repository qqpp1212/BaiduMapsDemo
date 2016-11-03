package com.feicuiedu.baidumapdemo;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

/**
 * Created by 123 on 2016/11/3.
 */

public class BaiduApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        // 进行初始化
        SDKInitializer.initialize(getApplicationContext());
    }
}
