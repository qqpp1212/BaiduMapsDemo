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
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private RelativeLayout mainLayout;
    private LocationClient locationClient;
    private LatLng mMyLocation;

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

        // 动态创建一个MapView
        mMapView = new MapView(this,options);
        // 将MapView添加到布局上
        mainLayout.addView(mMapView,0);

        mBaiduMap = mMapView.getMap();

        // 为地图设置状态监听
        mBaiduMap.setOnMapStatusChangeListener(mapStatusListener);

        mBaiduMap.setOnMarkerClickListener(MarkerListener);

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

//            Toast.makeText(MainActivity.this, "状态变化：纬度：" + mapStatus.target.latitude + "经度：" + mapStatus.target.longitude, Toast.LENGTH_SHORT).show();

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
                 *      定位不准确：默认展示gcj02方式，位置有偏差，我们定位的时候设置bd09ll
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
//                option.setScanSpan(5000);// 设置扫描周期

                // 添加配置的信息
                locationClient.setLocOption(option);

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

            /**
             * 定位到之后弹吐司改变成添加定位的标志，移动到我们的位置
             */
//            Toast.makeText(MainActivity.this, "经度："+lng+"纬度："+lat, Toast.LENGTH_SHORT).show();

            /**
             * 添加定位的标志
             *
             * 1. 拿到定位的信息
             * 2. 给地图设置上定位信息
             */

            MyLocationData myLocationData = new MyLocationData.Builder()
                    .latitude(lat)// 纬度
                    .longitude(lng)// 经度
                    .accuracy(100f)// 定位的精度的大小
                    .build();
            mBaiduMap.setMyLocationData(myLocationData);

            /**
             * 移动到我们的位置
             *
             * 1. 有我们定位的位置
             * 2. 移动的话，地图状态是不是发生变化了呢？要移动到定位的位置上去
             * 3. 地图状态的位置设置我们的位置
             * 4. 地图的状态变化了？我们需要对地图的状态进行更新
             */

            // 我们定位的位置
            mMyLocation = new LatLng(lat,lng);

            moveToMyLocation();

            addMarker(new LatLng(lat+0.1,lng+0.1));

        }
    };

    // 移动到我们定位的位置去
    private void moveToMyLocation() {
        // 主要是为将地图的位置设置成我们当前的位置
        MapStatus mapStatus = new MapStatus.Builder()
                .target(mMyLocation)
                .rotate(0)// 作用是摆正地图
                .zoom(19)
                .build();

        // 更新地图的状态
        MapStatusUpdate update = MapStatusUpdateFactory.newMapStatus(mapStatus);
        mBaiduMap.animateMapStatus(update);
    }

    private BitmapDescriptor dot = BitmapDescriptorFactory.fromResource(R.drawable.treasure_dot);
    private BitmapDescriptor dot_click = BitmapDescriptorFactory.fromResource(R.drawable.treasure_expanded);

    // 添加Marker
    private void addMarker(LatLng latLng){
        /**
         * 在某一位置，添加标注物
         * 1. 目的：地图上添加一个标志
         * 2. 实现步骤：主要的两个方面
         *      1. 确定你要添加标注物的位置：经纬度
         *      2. 确定你要添加的图标
         *
         */
        // MarkerOptions是抽象类OverlayOptions的子类
        MarkerOptions options = new MarkerOptions();
        options.position(latLng);
        options.icon(dot);

        // 添加标注物
        mBaiduMap.addOverlay(options);
    }

    private BaiduMap.OnMarkerClickListener MarkerListener = new BaiduMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            /**
             * marker点击的时候会触发这个方法
             * 展示一个信息窗口：文本、图片、。。。。
             *
             * 1. 目的：点击之后，展示出一个InfoWindow
             * 2. 实现：1. 创建一个Infowindow
             *          2. 设置你展示的是什么？
             *          3. 还是在地图上展示出来
             *
             */
            InfoWindow infoWindow = new InfoWindow(dot_click, marker.getPosition(), 0, new InfoWindow.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick() {

                }
            });
            mBaiduMap.showInfoWindow(infoWindow);

            return false;
        }
    };
}
