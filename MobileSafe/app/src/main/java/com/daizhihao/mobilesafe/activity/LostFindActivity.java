package com.daizhihao.mobilesafe.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.daizhihao.mobilesafe.R;

/**
 * 手机防盗页面
 * Created by Administrator on 2016/4/27.
 */
public class LostFindActivity extends AppCompatActivity{

    private SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPref = getSharedPreferences("config", MODE_PRIVATE);
        boolean configed = mPref.getBoolean("configed", false);//是否进行过设置向导
        if (configed){
            //显示该页
            setContentView(R.layout.activity_lost_find);
        }else {
            //跳转到向导页
            startActivity(new Intent(LostFindActivity.this,Setup1Activity.class));
            finish();
        }
    }

    /**
     *重新进去向导
     * @param view
     */
    public void reEnter(View view) {
        startActivity(new Intent(this,Setup1Activity.class));
        finish();
    }
}
