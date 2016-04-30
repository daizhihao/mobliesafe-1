package com.daizhihao.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.daizhihao.mobilesafe.R;

/**
 * 设置第三个向导页
 * Created by Administrator on 2016/4/27.
 */
public class Setup3Activity extends BaseSetupActivity {

    private EditText etPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
        etPhone = (EditText) findViewById(R.id.et_phone);
        String safe_phone = mPref.getString("safe_phone", "");
        etPhone.setText(safe_phone);
    }
    @Override
    public void showPreviousPage() {
        startActivity(new Intent(this, Setup2Activity.class));
        finish();
        // 两个界面切换的动画
        overridePendingTransition(R.anim.tran_previous_in,
                R.anim.tran_previous_out);// 进入动画和退出动画
    }

    @Override
    public void shownextPage() {
        String phone = etPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "安全号码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (phone.length() == 11){
            mPref.edit().putString("safe_phone",phone).commit();
            startActivity(new Intent(this, Setup4Activity.class));
            finish();
            // 两个界面切换的动画, 进入动画和退出动画
            overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
        }else {
            Toast.makeText(this, "请输入正确的安全号码", Toast.LENGTH_SHORT).show();
        }
    }

    public void save_content(View view) {
        shownextPage();
    }
}
