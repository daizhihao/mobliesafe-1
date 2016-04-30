package com.daizhihao.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;

import com.daizhihao.mobilesafe.R;
import com.daizhihao.mobilesafe.view.SettingItemView;

/**
 * 设置第二个向导页
 * Created by Administrator on 2016/4/27.
 */
public class Setup2Activity extends BaseSetupActivity {

    private SettingItemView sivSim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
        sivSim = (SettingItemView) findViewById(R.id.siv_sim);
        String sim = mPref.getString("sim", null);
        if (!TextUtils.isEmpty(sim)){
            sivSim.setChecked(true);
        }else {
            sivSim.setChecked(false);
        }
        sivSim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sivSim.isChecked()){
                    sivSim.setChecked(false);
                    mPref.edit().remove("sim").commit();// 删除已绑定的sim卡
                }else {
                    sivSim.setChecked(true);
                    TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                    String simSerialNumber = tm.getSimSerialNumber();// 获取sim卡序列号
                    mPref.edit().putString("sim",simSerialNumber).commit();
                }
            }
        });
    }

    @Override
    public void showPreviousPage() {
        startActivity(new Intent(this, Setup1Activity.class));
        finish();
        // 两个界面切换的动画
        overridePendingTransition(R.anim.tran_previous_in,
                R.anim.tran_previous_out);// 进入动画和退出动画
    }

    @Override
    public void shownextPage() {
//        String sim = mPref.getString("sim", null);
//        if (TextUtils.isEmpty(sim)) {
//            Toast.makeText(this, "必须先绑定sim卡哦!", Toast.LENGTH_SHORT).show();
//            return;
//        }
        startActivity(new Intent(this,Setup3Activity.class));
        finish();
        // 两个界面切换的动画, 进入动画和退出动画
        overridePendingTransition(R.anim.tran_in,R.anim.tran_out);
    }
}
