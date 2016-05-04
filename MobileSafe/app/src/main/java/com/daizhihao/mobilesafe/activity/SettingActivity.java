package com.daizhihao.mobilesafe.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.daizhihao.mobilesafe.R;
import com.daizhihao.mobilesafe.service.AddressService;
import com.daizhihao.mobilesafe.service.CallSafeService;
import com.daizhihao.mobilesafe.utils.ServiceStatusUtil;
import com.daizhihao.mobilesafe.view.SettingClickView;
import com.daizhihao.mobilesafe.view.SettingItemView;

/**
 * 设置界面
 * Created by Administrator on 2016/4/26.
 */
public class SettingActivity extends AppCompatActivity {

    private SettingItemView siv_Update;// 设置升级
    private SettingItemView siv_Address;//设置归属地显示
    private SettingItemView siv_callsafe;// 修改黑名单
    private SettingClickView scv_AddressStyle;// 修改风格
    private SettingClickView scv_AddressLocation;// 修改归属地位置
    private SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mPref = getSharedPreferences("config", MODE_PRIVATE);
        initUpdateView();
        initAddressView();
        initAddressStyle();
        initAddressLocation();
        initBlackView();
    }

    /**
     * 修改黑名单号码
     */
    private void initBlackView() {
        siv_callsafe = (SettingItemView) findViewById(R.id.siv_callsafe);

        // 根据归属地服务是否运行来更新checkbox
        boolean serviceRunning = ServiceStatusUtil.isServiceRunning
                (this, "com.daizhihao.mobilesafe.service.CallSafeService");
        if (serviceRunning) {
            siv_callsafe.setChecked(true);
        } else {
            siv_callsafe.setChecked(false);
        }
        siv_callsafe.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (siv_callsafe.isChecked()) {
                    siv_callsafe.setChecked(false);
                    stopService(new Intent(SettingActivity.this,
                            CallSafeService.class));// 停止归属地服务
                } else {
                    siv_callsafe.setChecked(true);
                    startService(new Intent(SettingActivity.this,
                            CallSafeService.class));// 开启归属地服务
                }
            }
        });
    }

    /**
     * 初始化自动更新开关
     */
    private void initUpdateView() {
        siv_Update = (SettingItemView) findViewById(R.id.siv_update);
        boolean autoUpdate = mPref.getBoolean("auto_update", true);
        if (autoUpdate) {
            siv_Update.setChecked(true);
        } else {
            siv_Update.setChecked(false);
        }
        siv_Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 判断当前的勾选状态
                if (siv_Update.isChecked()) {
                    // 设置不勾选
                    siv_Update.setChecked(false);
                    // 更新sp
                    mPref.edit().putBoolean("auto_update", false).commit();
                } else {
                    siv_Update.setChecked(true);
                    // 更新sp
                    mPref.edit().putBoolean("auto_update", true).commit();
                }
            }
        });
    }

    /**
     * 初始化归属地开关
     */
    private void initAddressView() {
        siv_Address = (SettingItemView) findViewById(R.id.siv_address);
        // 根据归属地服务是否运行来更新checkbox
        boolean serviceRunning = ServiceStatusUtil.isServiceRunning
                (this, "com.daizhihao.mobilesafe.service.AddressService");
        if (serviceRunning) {
            siv_Address.setChecked(true);
        } else {
            siv_Address.setChecked(false);
        }
        siv_Address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (siv_Address.isChecked()) {
                    siv_Address.setChecked(false);
                    stopService(new Intent(SettingActivity.this, AddressService.class));// 停止归属地服务
                } else {
                    siv_Address.setChecked(true);
                    startService(new Intent(SettingActivity.this, AddressService.class));// 开启归属地服务
                }
            }
        });
    }
    final String[] items = new String[] { "半透明", "活力橙", "卫士蓝", "金属灰", "苹果绿" };
    /**
     * 修改提示框显示风格
     */
    private void initAddressStyle() {
        scv_AddressStyle = (SettingClickView) findViewById(R.id.scv_address_style);
        scv_AddressStyle.setTitle("归属框显示风格");
        int address_style = mPref.getInt("address_style", 0);
        scv_AddressStyle.setDesc(items[address_style]);
        scv_AddressStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSingleChooseDailog();
            }
        });
    }

    /**
     * 弹出选择风格的单选框
     */
    private void showSingleChooseDailog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("归属地提示框风格");
        int address_style = mPref.getInt("address_style", 0);
        builder.setSingleChoiceItems(items, address_style, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPref.edit().putInt("address_style", which).commit();
                scv_AddressStyle.setDesc(items[which]);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }
    /**
     * 修改归属地显示位置
     */
    private void initAddressLocation() {
        scv_AddressLocation = (SettingClickView) findViewById(R.id.scv_address_location);
        scv_AddressLocation.setTitle("归属地提示框显示位置");
        scv_AddressLocation.setDesc("设置归属地提示框的显示位置");

        scv_AddressLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this,
                        DragViewActivity.class));
            }
        });
    }
}
