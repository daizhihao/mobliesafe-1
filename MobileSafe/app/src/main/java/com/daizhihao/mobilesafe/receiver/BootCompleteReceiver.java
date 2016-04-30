package com.daizhihao.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * 监听手机开机启动的广播
 * Created by Administrator on 2016/4/28.
 */
public class BootCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        boolean protect = sp.getBoolean("protect", false);
        if (protect) {
            String sim = sp.getString("sim", null);//拿到保存过得sim卡信息
            if (!TextUtils.isEmpty(sim)) {
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                String currentSim = tm.getSimSerialNumber();//获取当前的sim卡信息
                if (sim.equals(currentSim)) {
                    //说明手机安全
                } else {
                    //sim卡已经更换
                    String safe_phone = sp.getString("safe_phone", "");
                    // 发送短信给安全号码
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(safe_phone, null, "sim卡已经更换", null, null);
                }
            }
        }
    }
}
