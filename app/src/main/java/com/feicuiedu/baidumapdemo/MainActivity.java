package com.feicuiedu.baidumapdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MapView mMapView;
    private BaiduMap mBaiduMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * 1.找到MapView
         * 2.获取操作地图的控制器
         * 3.卫星视图和普通视图的切换
         */
        mMapView = (MapView) findViewById(R.id.mapview);
        mBaiduMap = mMapView.getMap();

        // 地图状态
        MapStatus mapStatus = new MapStatus.Builder()
                .overlook(0)// 地图俯仰的角度  -45--0
                .zoom(15)//缩放的级别  3-21
                .build();

        BaiduMapOptions options = new BaiduMapOptions()
                .zoomControlsEnabled(false)// 不显示缩放的控件
                .zoomGesturesEnabled(true)// 是否允许缩放的手势
                // 具体查看API
                .mapStatus(mapStatus);

        // 目前来说，设置只能通过MapView的构造方法来添加,所以Demo里面是在布局中添加MapView
        // 后面项目实施会动态创建
//        MapView mapView = new MapView(this,options);

        // 为地图设置状态监听
        mBaiduMap.setOnMapStatusChangeListener(mapStatusListener);

        initView();


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

    private void initView() {
        Button btn_sate = (Button) findViewById(R.id.btn_sate);
        Button btn_location = (Button) findViewById(R.id.btn_location);
        btn_sate.setOnClickListener(this);
        btn_location.setOnClickListener(this);
    }

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
                 */

//                mBaiduMap.setMyLocationEnabled(true);// 打开定位
//                LocationClient locationClient = new LocationClient(getApplicationContext());
//                LocationClientOption option = new LocationClientOption();

            }
            break;
        }
    }
}
