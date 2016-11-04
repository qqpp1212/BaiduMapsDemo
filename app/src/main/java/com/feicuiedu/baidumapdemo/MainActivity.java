package com.feicuiedu.baidumapdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private RelativeLayout mainLayout;
    private LocationClient locationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainLayout = (RelativeLayout) findViewById(R.id.activity_main);

        /**
         * 1.找到MapView
         * 2.获取操作地图的控制器
         * 3.卫星视图和普通视图的切换
         * 4. 其他操作
         */
        mMapView = (MapView) findViewById(R.id.mapview);


        // 地图状态
        MapStatus mapStatus = new MapStatus.Builder()
                .overlook(0)// 地图俯仰的角度  -45--0
                .zoom(15)//缩放的级别  3-21
                .build();

        BaiduMapOptions options = new BaiduMapOptions()
                .zoomControlsEnabled(false)// 不显示缩放的控件
                .zoomGesturesEnabled(true)// 是否允许缩放的手势
                .compassEnabled(true)// 指南针
                // 具体查看API
                .mapStatus(mapStatus);

        // 目前来说，设置只能通过MapView的构造方法来添加,所以Demo里面是在布局中添加MapView
        // 后面项目实施会动态创建
//        mMapView = new MapView(this,options);

        mBaiduMap = mMapView.getMap();

        // 为地图设置状态监听
        mBaiduMap.setOnMapStatusChangeListener(mapStatusListener);

        initView();
    }

    private void initView() {
        Button btn_sate = (Button) findViewById(R.id.btn_sate);
        Button btn_location = (Button) findViewById(R.id.btn_location);
        btn_sate.setOnClickListener(this);
        btn_location.setOnClickListener(this);
    }

    // 地图状态的监听
    private BaiduMap.OnMapStatusChangeListener mapStatusListener = new BaiduMap.OnMapStatusChangeListener() {
        @Override
        public void onMapStatusChangeStart(MapStatus mapStatus) {

        }

        @Override
        public void onMapStatusChange(MapStatus mapStatus) {

        }

        @Override
        public void onMapStatusChangeFinish(MapStatus mapStatus) {

            Toast.makeText(MainActivity.this, "状态变化：纬度：" + mapStatus.target.latitude + "经度：" + mapStatus.target.longitude, Toast.LENGTH_SHORT).show();

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sate: {
                //切换卫星和普通的视图

                // 判断一下是什么视图
                if (mBaiduMap.getMapType() == BaiduMap.MAP_TYPE_SATELLITE) {
                    mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                    return;
                }
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
            }
            break;
            case R.id.btn_location: {
                // 定位相关

                /**
                 * 1. 开启定位图层
                 * 2. 初始化LocationClient
                 * 3. 配置一些定位相关的参数LocationClientOption
                 * 4. 设置监听，定位的监听
                 * 5. 开启定位
                 *
                 * 注意：不要使用模拟器，真机，6.0 以上需要添加运行时权限的
                 *      定位不准确：默认展示gcj02方式，位置有偏差，我们定位的时候设置
                 */

                // 打开定位
                mBaiduMap.setMyLocationEnabled(true);

                // 初始化定位
                locationClient = new LocationClient(getApplicationContext());

                // 配置
                LocationClientOption option = new LocationClientOption();

                // 定位相关设置，更多设置查看API
                option.setOpenGps(true);// 打开GPS
                option.setCoorType("bd09ll");// 设置坐标类型，默认gcj02
                option.setIsNeedAddress(true);// 默认不需要
                option.setScanSpan(5000);// 设置扫描周期

                // 设置监听
                locationClient.registerLocationListener(locationListener);

                // 开启定位
                locationClient.start();

                // 针对于某些机型，开启请求位置会失败
                locationClient.requestLocation();

            }
            break;
        }
    }

    private BDLocationListener locationListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            /**
             * 在定位监听里面，可以根据我们的结果来处理，显示定位的数据。。。。
             */

            if (bdLocation==null){
                // 没有定位信息，重新定位，重新请求定位信息
                locationClient.requestLocation();// 请求定位
                return;
            }

            double lng = bdLocation.getLongitude();// 获取经度
            double lat = bdLocation.getLatitude();// 获取纬度

            Toast.makeText(MainActivity.this, "经度："+lng+"纬度："+lat, Toast.LENGTH_SHORT).show();

        }
    };
}
