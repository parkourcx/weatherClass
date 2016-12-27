package com.example.chenxiang.weatherapp;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import com.example.chenxiang.weatherapp.bean.City;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenxiang on 2016/12/27.
 */

public class MyApplication extends Application {
    private static final String TAG = "MyAPP";
    private static MyApplication mApplication;
    private CityDB mCityDB;
    private List<City> mCityList;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "MyApplication->Oncreate");
        mApplication = this;
        mCityDB = openCityDB();
        initCityList();
    }

    public static MyApplication getInstance() {
        return mApplication;
    }

    private void initCityList() {
        mCityList = new ArrayList<City>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                prepareCityList();
            }
        }).start();
    }

    private boolean prepareCityList() {
        mCityList = mCityDB.getAllCity();
        for (City city : mCityList) {
            String cityName = city.getCity();
            //  Log.d(TAG,cityName);
        }
        return true;
    }

    public List<City> getCityList() {
        return mCityList;
    }

    public CityDB openCityDB() {
//        String path = "/data"
//                + Environment.getDataDirectory().getAbsolutePath()
//                + File.separator + getPackageName()
//                + File.separator + "databases";
//               // + File.separator
//                //+ CityDB.CITY_DB_NAME;
//        File db = new File(path);
//        Log.d(TAG,path);
//        if (!db.exists()) {
//
//            String pathfolder = "/data"
//                    + Environment.getDataDirectory().getAbsolutePath()
//                    + File.separator + getPackageName()
//                    + File.separator + "databases"
//                    + File.separator;
//            File dirFirstFolder = new File(pathfolder);
//            if(!dirFirstFolder.exists()){
//                dirFirstFolder.mkdirs();
//                Log.i("MyApp","mkdirs");
//            }
//            Log.i("MyApp","db is not exists");
//            try {
//                InputStream is = getAssets().open("city.db");
//                FileOutputStream fos = new FileOutputStream(db);
//                int len = -1;
//                byte[] buffer = new byte[1024];
//                while ((len = is.read(buffer)) != -1) {
//                    fos.write(buffer, 0, len);
//                    fos.flush();
//                }
//                fos.close();
//                is.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//                System.exit(0);
//            } }
//        return new CityDB(this, path);

        String path = "/data"
                + Environment.getDataDirectory().getAbsolutePath()
                + File.separator + getPackageName()
                + File.separator + "databases";
//                + File.separator
//                + CityDB.CITY_DB_NAME;

        Log.d(TAG, path);
        File db = new File(path);
        if (!db.exists()) {
            db.mkdir();
        }
        //数据库
        File dbfile = new File(db, "city.db");
        try {
            if (!dbfile.exists()) {
                dbfile.createNewFile();
            }
            InputStream is = getAssets().open("city.db");

            FileOutputStream fos = new FileOutputStream(dbfile);
            byte[] buffere = new byte[is.available()];
            is.read(buffere);
            fos.write(buffere);
            is.close();
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new CityDB(this, path + "/" + CityDB.CITY_DB_NAME);
    }
}
