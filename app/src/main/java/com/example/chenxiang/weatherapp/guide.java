package com.example.chenxiang.weatherapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by chenxiang on 2016/12/27.
 */
public class guide extends Activity implements ViewPager.OnPageChangeListener{
    private View p1,p2,p3;
    private ViewPager vp;
    private List<View> viewList;
    private ImageButton imageButton;
    private ImageView[] dots;
    private int[] ids={R.id.iv1,R.id.iv2,R.id.iv3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide);
        vp=(ViewPager)findViewById(R.id.viewPager);
        LayoutInflater inflater=LayoutInflater.from(this);
        p1=inflater.inflate(R.layout.page1,null);
        p2=inflater.inflate(R.layout.page2,null);
        p3=inflater.inflate(R.layout.page3,null);

        viewList=new ArrayList<>();
        viewList.add(p1);
        viewList.add(p2);
        viewList.add(p3);

        initDots ();

        imageButton=(ImageButton)viewList.get(2).findViewById(R.id.start);
        imageButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    v.setBackgroundResource(R.drawable.on);
                    TimerTask task = new TimerTask(){
                        public void run(){
                            //execute the task
                            Intent intent=new Intent(guide.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    };
                    Timer timer = new Timer();
                    timer.schedule(task, 1000);

                }else if(event.getAction()==MotionEvent.ACTION_UP){
                    v.setBackgroundResource(R.drawable.on);
                }
                return false;
            }
        });

        PagerAdapter pagerAdapter=new PagerAdapter() {
            @Override
            public int getCount() {
                Log.d("myApp", "getCount:" + viewList.size());
                return viewList.size();
            }
            @Override
            public boolean isViewFromObject(View view, Object o) {
                Log.d("myApp","isViewFromObject");
                return (view==o);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                Log.d("myApp","destroyItem");
                container.removeView(viewList.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {

                container.addView(viewList.get(position));
                Log.d("myApp","instantiateItem:"+viewList.get(position));
                return viewList.get(position);
            }
        };
        vp.setAdapter(pagerAdapter);
        vp.setOnPageChangeListener(this);
    }
    void initDots (){
        dots=new ImageView[viewList.size()];
        for(int i=0;i<viewList.size();i++){
            dots[i]=(ImageView)findViewById(ids[i]);
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for(int a=0;a<ids.length;a++){
            if(a==position){
                dots[a].setImageResource(R.drawable.page_indicator_focused);
            }else{
                dots[a].setImageResource(R.drawable.page_indicator_unfocused);
            }
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
