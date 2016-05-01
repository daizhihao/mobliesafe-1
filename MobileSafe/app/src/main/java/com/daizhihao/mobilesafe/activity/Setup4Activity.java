package com.daizhihao.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.daizhihao.mobilesafe.R;

/**
 * 设置第四个向导页
 * Created by Administrator on 2016/4/27.
 */
public class Setup4Activity extends BaseSetupActivity {

    private CheckBox cbProtect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
        cbProtect = (CheckBox) findViewById(R.id.cb_protect);
        boolean protect = mPref.getBoolean("protect", false);
        // 根据sp保存的状态,更新checkbox
        if (protect) {
            cbProtect.setText("防盗保护已经开启");
            cbProtect.setChecked(true);
        } else {
            cbProtect.setText("防盗保护没有开启");
            cbProtect.setChecked(false);
        }
        // 当checkbox发生变化时,回调此方法
        cbProtect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 根据sp保存的状态,更新checkbox
                if (isChecked) {
                    cbProtect.setText("防盗保护已经开启");
                    mPref.edit().putBoolean("protect", true).commit();
                } else {
                    cbProtect.setText("防盗保护没有开启");
                    mPref.edit().putBoolean("protect", false).commit();
                }
            }
        });
    }

    @Override
    public void showPreviousPage() {
        startActivity(new Intent(this, Setup3Activity.class));
        finish();

        // 两个界面切换的动画
        overridePendingTransition(R.anim.tran_previous_in,
                R.anim.tran_previous_out);// 进入动画和退出动画
    }

    @Override
    public void shownextPage() {
        startActivity(new Intent(this,LostFindActivity.class));
        finish();
        // 更新sp,表示已经展示过设置向导了,下次进来就不展示啦
        mPref.edit().putBoolean("configed", true).commit();
        // 两个界面切换的动画, 进入动画和退出动画
        overridePendingTransition(R.anim.tran_in,R.anim.tran_out);
    }
}
