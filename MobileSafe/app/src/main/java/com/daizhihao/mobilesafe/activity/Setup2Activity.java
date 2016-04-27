package com.daizhihao.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.daizhihao.mobilesafe.R;

/**
 * 设置第二个向导页
 * Created by Administrator on 2016/4/27.
 */
public class Setup2Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
    }
    public void next(View view) {
        startActivity(new Intent(this, Setup3Activity.class));
        finish();
        // 两个界面切换的动画, 进入动画和退出动画
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
    }
    public void previous(View view) {
        startActivity(new Intent(this,Setup1Activity.class));
        finish();
        // 两个界面切换的动画, 进入动画和退出动画
        overridePendingTransition(R.anim.tran_previous_in, R.anim.tran_previous_out);
    }
}
