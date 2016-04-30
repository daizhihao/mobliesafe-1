package com.daizhihao.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import com.daizhihao.mobilesafe.R;
import com.daizhihao.mobilesafe.service.LocationService;

/**
 * 拦截短信
 * Created by Administrator on 2016/4/29.
 */
public class SmsReceiver extends BroadcastReceiver{

    private SharedPreferences mPref;

    @Override
    public void onReceive(Context context, Intent intent) {
        //拿到短信包
        Object[] objects = (Object[]) intent.getExtras().get("pdus");
        for (Object object:objects) {
            // 短信最多140字节,
            // 超出的话,会分为多条短信发送,所以是一个数组,因为我们的短信指令很短,所以for循环只执行一次
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) object);
            String originatingAddress = smsMessage.getOriginatingAddress();// 短信来源号码
            String messageBody = smsMessage.getMessageBody();// 短信内容
            if ("#*alarm*#".equals(messageBody)){
                // 播放报警音乐, 即使手机调为静音,也能播放音乐, 因为使用的是媒体声音的通道,和铃声无关
                MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.alarm);
                mediaPlayer.setVolume(1f, 1f);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
                abortBroadcast();//中断短信
            }else if("#*location*#".equals(messageBody)){
                //获取经纬度坐标,开启服务
                context.startActivity(new Intent(context, LocationService.class));
                mPref = context.getSharedPreferences("config", Context.MODE_PRIVATE);
                String location = mPref.getString("location", "getting location...");
                String safe_phone = mPref.getString("safe_phone", "");
                // 发送短信给安全号码
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(safe_phone, null,location , null, null);
                abortBroadcast();// 中断短信的传递, 从而系统短信app就收不到内容了
            }else if ("#*wipedata*#".equals(messageBody)) {
                abortBroadcast();
            } else if ("#*lockscreen*#".equals(messageBody)) {
                abortBroadcast();
            }
        }
    }
}
