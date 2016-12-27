package com.example.chenxiang.weatherapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chenxiang.weatherapp.NetUtil.NetUtil;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.example.chenxiang.weatherapp.bean.todayWeather;

/**
 * Created by chenxiang on 16/10/17.
 */

public class MainActivity extends Activity implements View.OnClickListener {
    private static final int UPDATE_TODAY_WEATHER = 1;
    private ImageView mCitySelect;
    private ImageView mUpdateBtn;
    private TextView cityTv,timeTv,humidityTv,weekTv,pmData,pmQualityTv,temperatureTv,climateTv,windTv,titleCityName;
    String cityCode,currentCityNumber,nextCityNumber;
    private ImageView weatherImg,pmImg;
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case UPDATE_TODAY_WEATHER:
                    updateTodayWeather((todayWeather) msg.obj);
                    break;
                default:
                    break; }
        } };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_info);//加载布局

        mUpdateBtn = (ImageView) findViewById(R.id.title_update_btn);
        mUpdateBtn.setOnClickListener(this);

        if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
            Log.d("myWeather", "网络可用");
            Toast.makeText(MainActivity.this, "网络可用!", Toast.LENGTH_LONG).show();
        } else {
            Log.d("myWeather", "网络不可用");
            Toast.makeText(MainActivity.this, "网络不可用!", Toast.LENGTH_LONG).show();
        }
        mCitySelect = (ImageView) findViewById(R.id.title_city_manager);
        mCitySelect.setOnClickListener(this);
        initView();
    }

    //更新天气
    void updateTodayWeather(todayWeather todayweather){
        titleCityName.setText(todayweather.getCity()+"天气");
        cityTv.setText(todayweather.getCity());
        timeTv.setText(todayweather.getUpdatetime()+ "发布");
        humidityTv.setText("湿度:"+todayweather.getShidu());
        pmData.setText(todayweather.getPm25());
        pmQualityTv.setText(todayweather.getQuality());
        weekTv.setText(todayweather.getDate());
        temperatureTv.setText(todayweather.getHigh()+"~"+todayweather.getLow());

        climateTv.setText(todayweather.getType()); windTv.setText("风力:"+todayweather.getFengli()); Toast.makeText(MainActivity.this,"更新成功!",Toast.LENGTH_SHORT).show();
    }
    //初始化控件
    void initView(){

        titleCityName = (TextView)findViewById(R.id.title_city_name);
        cityTv = (TextView) findViewById(R.id.city);
        timeTv = (TextView) findViewById(R.id.time);
        humidityTv = (TextView) findViewById(R.id.humidity);
        weekTv = (TextView) findViewById(R.id.week_today);
        pmData = (TextView) findViewById(R.id.pm_data);
        pmQualityTv = (TextView) findViewById(R.id.pm2_5_quality);
        pmImg = (ImageView) findViewById(R.id.pm2_5_img);
        temperatureTv = (TextView) findViewById(R.id.temperature);
        climateTv = (TextView) findViewById(R.id.climate);
        windTv = (TextView) findViewById(R.id.wind);
        weatherImg = (ImageView) findViewById(R.id.weather_img);
       // city_name_Tv.setText("N/A");
        cityTv.setText("N/A");
        timeTv.setText("N/A");
        humidityTv.setText("N/A");
        //pmDataTv.setText("N/A");
        pmQualityTv.setText("N/A");
        weekTv.setText("N/A");
        temperatureTv.setText("N/A");
        climateTv.setText("N/A");
        windTv.setText("N/A");
        queryWeatherCode("101010100");
//        ///////
//        cityTv=(TextView)findViewById(R.id.city);
//        timeTv=(TextView)findViewById(R.id.time);
//        humidityTv=(TextView)findViewById(R.id.hunidity);
//        weekTv=(TextView)findViewById(R.id.week_today);
//        pmData=(TextView)findViewById(R.id.pm_data);
//        pmQualityTv=(TextView)findViewById(R.id.pmQuality);
//        temperatureTv=(TextView)findViewById(R.id.temperature);
//        climateTv=(TextView)findViewById(R.id.climate);
//        windTv=(TextView)findViewById(R.id.wind);
//        weatherImg=(ImageView)findViewById(R.id.weather_img);
//        pmImg=(ImageView)findViewById(R.id.pmImg);
//        titleCityName=(TextView)findViewById(R.id.title_city_name);
////        cityTv.setText("N/A");
////        timeTv.setText("N/A");
////        humidityTv.setText("N/A");
////        pmData.setText("N/A");
////        pmQualityTv.setText("N/A");
////        weekTv.setText("N/A");
////        temperatureTv.setText("N/A");
////        climateTv.setText("N/A");
////        windTv.setText("N/A");

    }

    //通过SharedPreferences读取城市id，如果没有定义则缺省为101010100(北京城市 ID)。
    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.title_city_manager){
            Intent i = new Intent(this,SelectCity.class);
            //startActivity(i);
            startActivityForResult(i,1);
        }
        if (view.getId() == R.id.title_update_btn) {
            SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
            String cityCode = sharedPreferences.getString("main_city_code", "101010100");
            Log.d("myWeather", cityCode);

            if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
                Log.d("myWeather", "网络可用");
                queryWeatherCode(cityCode);
            } else {
                Log.d("myWeather", "网络不可用");
                Toast.makeText(MainActivity.this, "网络不可用!", Toast.LENGTH_LONG).show();
            }
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String newCityCode= data.getStringExtra("cityCode"); Log.d("myWeather", "选择的城市代码为"+newCityCode);
             {
                if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE){
                Log.d("myWeather", "网络OK");
                queryWeatherCode(newCityCode);
            }else
            {
                Log.d("myWeather", "网络挂了");
                Toast.makeText(MainActivity.this, "网络挂了!", Toast.LENGTH_LONG).show();
            }
        }
        }}

    //使用**获取网络数据
    private void queryWeatherCode(String cityCode) {
        final String address = "http://wthrcdn.etouch.cn/WeatherApi?citykey=" + cityCode;
        Log.d("myWeather", address);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection con = null;
                todayWeather todayweather = null;
                try {
                    URL url = new URL(address);
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.setConnectTimeout(4000);
                    con.setReadTimeout(5000);
                    InputStream in = con.getInputStream(); //123123
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String str;
                    while ((str = reader.readLine()) != null) {
                        response.append(str);
                        Log.d("myWeather", str);

                    }
                    String responseStr = response.toString();
                    Log.d("myWeather", responseStr);
                    todayweather = parseXml(responseStr);
                    if(todayweather != null){
                        Log.d("myWeather",todayweather.toString());
                        Message msg =new Message();
                        msg.what = UPDATE_TODAY_WEATHER;
                        msg.obj=todayweather;
                        mHandler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (con != null) {
                        con.disconnect();
                    }
                }
            }
        }).start();
    }

    private todayWeather parseXml(String xmldata) {
        todayWeather todayWeather = null;
        try {
            int fengxiangCount = 0;
            int fengliCount = 0;
            int dateCount = 0;
            int highCount = 0;
            int lowCount = 0;
            int typeCount = 0;
            XmlPullParserFactory fac = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = fac.newPullParser();
            xmlPullParser.setInput(new StringReader(xmldata));
            int eventType = xmlPullParser.getEventType();
            Log.d("myapp", "parseXML");
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if (xmlPullParser.getName().equals("resp")) {
                            todayWeather = new todayWeather();
                        }
                        if (todayWeather != null) {
                            if (xmlPullParser.getName().equals("city")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setCity(xmlPullParser.getText());
                                Log.d("myapp", "city:" + xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("updatetime")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setUpdatetime(xmlPullParser.getText());
                                Log.d("myapp", "updatetime:" + xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("shidu")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setShidu(xmlPullParser.getText());
                                Log.d("myapp", "shidu:" + xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("wendu")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setWendu(xmlPullParser.getText());
                                Log.d("myapp", "wendu:" + xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("pm25")) {
                                eventType = xmlPullParser.next();
                                if(xmlPullParser.getText()==null){
                                    todayWeather.setPm25("35");
                                }else {
                                    todayWeather.setPm25(xmlPullParser.getText());
                                }
                                Log.d("myapp", "pm2.5:" + xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("quality")) {
                                eventType = xmlPullParser.next();
                                if(xmlPullParser.getText()==null){
                                    todayWeather.setQuality("空气良好");
                                }else{
                                    todayWeather.setQuality(xmlPullParser.getText());
                                }
                                Log.d("myapp", "qulity:" + xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("fengxiang") && fengxiangCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFengxiang(xmlPullParser.getText());
                                Log.d("myapp", "fengxiang:" + xmlPullParser.getText());
                                fengxiangCount++;
                            } else if (xmlPullParser.getName().equals("fengli") && fengliCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFengli(xmlPullParser.getText());
                                Log.d("myapp", "fengli:" + xmlPullParser.getText());
                                fengliCount++;
                            } else if (xmlPullParser.getName().equals("date") && dateCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setDate(xmlPullParser.getText());
                                Log.d("myapp", "date:" + xmlPullParser.getText());
                                dateCount++;
                            } else if (xmlPullParser.getName().equals("high") && highCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setHigh(xmlPullParser.getText());
                                Log.d("myapp", "high:" + xmlPullParser.getText());
                                highCount++;
                            } else if (xmlPullParser.getName().equals("low") && lowCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setLow(xmlPullParser.getText());
                                Log.d("myapp", "low:" + xmlPullParser.getText());
                                lowCount++;
                            } else if (xmlPullParser.getName().equals("type") && typeCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setType(xmlPullParser.getText());
                                Log.d("myapp", "type:" + xmlPullParser.getText());
                                typeCount++;
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                eventType = xmlPullParser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //td=todayWeather;
        return todayWeather;
    }

}