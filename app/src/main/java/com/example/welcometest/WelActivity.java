package com.example.welcometest;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class WelActivity extends Activity {

    private SharedPreferences mSharedPreferences;
    private static final int GO_HOME = 1; //进入主活动
    private static final int GO_GUIDE = 2;	//进入引导页
    private static final int ENTEER_DURATION = 1000;  //延时一秒
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GO_GUIDE:
                    startGuideActivity();
                    break;
                case GO_HOME:
                    startHomeActivitity();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wel);

        mSharedPreferences = getSharedPreferences("config",MODE_PRIVATE);
        init();

    }

    private void init() {
        boolean isFirstIn = mSharedPreferences.getBoolean("mIsFirstIn",true);
        if(isFirstIn){
            mHandler.sendEmptyMessageDelayed(GO_GUIDE,ENTEER_DURATION);
        }else{
            mHandler.sendEmptyMessageDelayed(GO_HOME,ENTEER_DURATION);
        }
    }

    private void startHomeActivitity(){
        Intent intent = new Intent(WelActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void startGuideActivity(){
        Intent intent = new Intent(WelActivity.this,GuideActivity.class);
        startActivity(intent);
        finish();
    }

}
