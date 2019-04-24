package com.example.welcometest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends Activity implements ViewPager.OnPageChangeListener {

    private List<View> mviewList;
    private ViewPager mViewPager;    //滑动页
    private ImageView[] mDotList;
    private int mLastPosition; //记录选中页面的位置

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initView();
        initViewPager();
        initDots();
    }

    //接口ViewPager.OnPageChangeListener需要重写的三个方法
    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }
    @Override
    public void onPageSelected(int i) { //当滚动页面为当前页面时
        setCurrentDotPosition(i);
    }
    @Override
    public void onPageScrollStateChanged(int i) {

    }
    private void setCurrentDotPosition(int i) {
        mDotList[i].setEnabled(true);
        mDotList[mLastPosition].setEnabled(false);
        mLastPosition = i;
    }


    class MyPagerAdapter extends PagerAdapter {  //滑动页的适配器

        private List<View> mImageViewList;
        private Context mContext;

        MyPagerAdapter(List<View> list, Context context) {
            super();
            mImageViewList = list;
            mContext = context;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            if (mImageViewList != null) {
                if (mImageViewList.size() > 0) {
                    container.addView(mImageViewList.get(position));
                    if(position == mImageViewList.size()-1){  //当单击了第三个引导页的立即体验跳到home页
                        ImageView imageView = (ImageView)mImageViewList.get(position).findViewById(R.id.iv_start);
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startHomeActivity();
                                setGuided(); //设置配置文件下次进入程序不用进入引导页
                            }
                        });
                    }
                    return mImageViewList.get(position);
                }
            }
            return null;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            if (mImageViewList != null) {
                if (mImageViewList.size() > 0) {
                    container.removeView(mImageViewList.get(position));
                }
            }
        }

        @Override
        public int getCount() {
            if (mImageViewList != null) {
                return mImageViewList.size();
            }
            return 0;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }
    }

    private void initView() { //给引导页面布局初始化
        LayoutInflater inflater = LayoutInflater.from(this);
        mviewList = new ArrayList<>();
        mviewList.add(inflater.inflate(R.layout.guide1_layout, null));
        mviewList.add(inflater.inflate(R.layout.guide2_layout, null));
        mviewList.add(inflater.inflate(R.layout.guide3_layout, null));
    }

    private void initViewPager() {   //初始化引导页面
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        MyPagerAdapter adapter = new MyPagerAdapter(mviewList, this);
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(this);
    }

    private void initDots() {  //初始化下面三个小点
        LinearLayout dotsLayout = (LinearLayout) findViewById(R.id.ll_dots_layout);
        mDotList = new ImageView[mviewList.size()];
        for (int i = 0; i < mviewList.size(); i++) {
            mDotList[i] = (ImageView) dotsLayout.getChildAt(i);
            mDotList[i].setEnabled(false);
        }
        mLastPosition = 0;
        mDotList[0].setEnabled(true);
    }

    private void startHomeActivity(){
        Intent intent = new Intent(GuideActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void setGuided(){
        SharedPreferences.Editor sp = getSharedPreferences("config",MODE_PRIVATE).edit();
        sp.putBoolean("mIsFirstIn",false);
        sp.apply();
    }

}