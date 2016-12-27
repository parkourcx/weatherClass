package com.example.chenxiang.weatherapp;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.example.chenxiang.weatherapp.bean.City;

import java.sql.SQLData;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by chenxiang on 2016/12/27.
 */

public class CityDB {
    public static final String CITY_DB_NAME ="city.db";
    private static final String CITY_TABLE_NAME="city";
    private SQLiteDatabase db;
    public CityDB(Context context,String path){
        db=context.openOrCreateDatabase(CITY_DB_NAME,Context.MODE_PRIVATE,null);

    }
    public List<City> getAllCity(){
        List<City> list=new ArrayList<>();
        Cursor c=db.rawQuery("SELECT * from "+"city",null);
        while(c.moveToNext()){
            String province=c.getString(c.getColumnIndex("province"));
            String city=c.getString(c.getColumnIndex("city"));
            String number=c.getString(c.getColumnIndex("number"));
            String allPY=c.getString(c.getColumnIndex("allpy"));
            String allFirstPY=c.getString(c.getColumnIndex("allfirstpy"));
            String firstPY=c.getString(c.getColumnIndex("firstpy"));
            City item = new City(province,city,number,firstPY,allPY,allFirstPY);
            list.add(item);
        }
        return list;
    }
}
