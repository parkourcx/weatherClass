package com.example.chenxiang.weatherapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


import com.example.chenxiang.weatherapp.bean.City;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.chenxiang.weatherapp.R.id.title_back;

/**
 * Created by chenxiang on 2016/12/26.
 */

public class SelectCity extends Activity implements View.OnTouchListener{
    private ImageView mBackBtn;
    private ListView mListView;
    private CityDB myCityDB;
    private MyApplication myapp;
    private List<City> myList;
    private TextView titleName;
    private EditText eSearch;
    private SimpleAdapter adapter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_city);
        eSearch=(EditText)findViewById(R.id.search_edit);
        Intent intent=this.getIntent();
        String cityName = intent.getStringExtra("cityName");
        titleName=(TextView)findViewById(R.id.title_name);
        if(cityName != null){
            titleName.setText("当前城市："+cityName);
        }

        mBackBtn = (ImageView)findViewById(R.id.title_back);
        mBackBtn.setOnTouchListener(this);
        myapp=(MyApplication)this.getApplication();
        myCityDB= myapp.openCityDB();
        mListView = (ListView)findViewById(R.id.lv);
        adapter=new SimpleAdapter(this,getdata(),R.layout.item, new String[]{"cityName","cityCode"}, new int[] {R.id.cityName,R.id.cityCode});
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new ItemClickEvent());
        eSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

//    @Override
//    public void onClick(View view) {
//    switch (view.getId()){
//        case title_back:
//            Intent i = new Intent();
//            i.putExtra("cityCode","101160101");
//            setResult(RESULT_OK,i);
//            finish();
//            break;
//        default:
//            break;
//    }
//    }

    //继承OnItemClickListener，当子项目被点击的时候触发
    private final class ItemClickEvent implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            HashMap<String,Object> city = (HashMap<String,Object>)mListView.getItemAtPosition(position);
            String code=city.get("cityCode").toString();
            Intent i=new Intent();
            i.putExtra("cityCode",code);
            setResult(RESULT_OK,i);
            Log.d("Mapppppppp",code);
            finish();

        }
    }

    private List<Map<String, Object>> getdata() {
        myList= new ArrayList<City>();
        myList=myCityDB.getAllCity();
        String []cityName=new String[myList.size()];
        String []cityCode=new String[myList.size()];
        int i=0;
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        for(City city : myList) {
            Map<String, Object> map = new HashMap<String, Object>();
            cityName[i] = city.getCity();
            cityCode[i] = city.getNumber();
            map.put("cityName",cityName[i]);
            map.put("cityCode",cityCode[i]);
            list.add(map);
            i++;
        }
        return list;
    }




    public boolean onTouch(View v, MotionEvent event) {
        switch(v.getId()){
            case R.id.title_back:
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    v.setBackgroundResource(R.drawable.backgrd);
//                    Intent intent = new Intent(this, SelectCity.class);
//                    startActivity(intent);
                    finish();
                }else if(event.getAction()==MotionEvent.ACTION_UP){
                    v.setBackgroundResource(R.drawable.backgrd);
                }
                break;
            default:
                break;
        }
        return false;
    }
}
