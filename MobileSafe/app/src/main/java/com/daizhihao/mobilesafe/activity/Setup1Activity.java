package com.daizhihao.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;

import com.daizhihao.mobilesafe.R;

/**
 * 设置第一个向导页
 * Created by Administrator on 2016/4/27.
 */
public class Setup1Activity extends BaseSetupActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);
    }

    @Override
    public void showPreviousPage() {

    }

    @Override
    public void shownextPage() {
        startActivity(new Intent(this, Setup2Activity.class));
        finish();
        // 两个界面切换的动画
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);// 进入动画和退出动画
    }
}
