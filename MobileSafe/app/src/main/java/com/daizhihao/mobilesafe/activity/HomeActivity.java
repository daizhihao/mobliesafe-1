package com.daizhihao.mobilesafe.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daizhihao.mobilesafe.R;
import com.daizhihao.mobilesafe.utils.MD5Utils;

/**
 * 主页面
 * Created by Administrator on 2016/4/25.
 */
public class HomeActivity extends AppCompatActivity {
    private GridView gvHome;

    private String[] mItems = new String[]{"手机防盗", "通讯卫士", "软件管理", "进程管理",
            "流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心"};

    private int[] mPics = new int[]{R.drawable.home_safe,
            R.drawable.home_callmsgsafe, R.drawable.home_apps,
            R.drawable.home_taskmanager, R.drawable.home_netmanager,
            R.drawable.home_trojan, R.drawable.home_sysoptimize,
            R.drawable.home_tools, R.drawable.home_settings};
    private SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mPref = getSharedPreferences("config", MODE_PRIVATE);
        gvHome = (GridView) findViewById(R.id.gv_home);
        gvHome.setAdapter(new HomeAdapter());
        //设置监听
        gvHome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        showPasswordDialog();
                        break;
                    case 1:
                        startActivity(new Intent(HomeActivity.this, CallSafeActivity.class));
                        break;
                    case 7:
                        startActivity(new Intent(HomeActivity.this, AToolsActivity.class));
                        break;
                    case 8:
                        startActivity(new Intent(HomeActivity.this, SettingActivity.class));
                        break;
                }
            }
        });
    }

    /**
     * 显示密码弹窗
     */
    private void showPasswordDialog() {
        // 判断是否设置密码
        String savedPassword = mPref.getString("password", null);
        if (!TextUtils.isEmpty(savedPassword)) {
            //已经输入过密码
            showPasswordinputDialog();
        } else {
            //若没有是设置密码则跳转到输入密码
            showPasswordsetDialog();
        }
    }

    /**
     * 输入密码弹框
     */
    private void showPasswordinputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog alertDialog = builder.create();
        View view = View.inflate(this, R.layout.dailog_input_password, null);
        alertDialog.setView(view, 0, 0, 0, 0);//为对话框设置自定义布局并设置4边留白保证在2.x的版本上运行没问题
        Button btn_OK = (Button) view.findViewById(R.id.btn_ok);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        final EditText ev_password = (EditText) view.findViewById(R.id.et_password);
        btn_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = ev_password.getText().toString();
                String savedPassword = mPref.getString("password", null);
                //判断String是否为空字符串
                if (!TextUtils.isEmpty(password)) {
                    if (MD5Utils.encode(password).equals(savedPassword)) {
                        startActivity(new Intent(HomeActivity.this, LostFindActivity.class));
                        alertDialog.dismiss();
                    } else {
                        Toast.makeText(HomeActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(HomeActivity.this, "输入框不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();//隐藏对话框
            }
        });
        alertDialog.show();
    }

    /**
     * 弹出设置密码对话框
     */
    private void showPasswordsetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog alertDialog = builder.create();
        View view = View.inflate(this, R.layout.dailog_set_password, null);
        alertDialog.setView(view, 0, 0, 0, 0);//为对话框设置自定义布局并设置4边留白保证在2.x的版本上运行没问题
        Button btn_OK = (Button) view.findViewById(R.id.btn_ok);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        final EditText ev_password = (EditText) view.findViewById(R.id.et_password);
        final EditText ev_passwordConfirm = (EditText) view.findViewById(R.id.et_password_confirm);
        btn_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = ev_password.getText().toString();
                String passwordConfirm = ev_passwordConfirm.getText().toString();
                //判断String是否为空字符串
                if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(passwordConfirm)) {
                    if (password.equals(passwordConfirm)) {
                        mPref.edit().putString("password", MD5Utils.encode(password)).commit();
                        startActivity(new Intent(HomeActivity.this, LostFindActivity.class));
                        alertDialog.dismiss();
                    } else {
                        Toast.makeText(HomeActivity.this, "两次密码输入不一致密码输入", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(HomeActivity.this, "输入框不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();//隐藏对话框
            }
        });
        alertDialog.show();
    }

    class HomeAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mItems.length;
        }

        @Override
        public Object getItem(int position) {
            return mItems[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(HomeActivity.this,
                    R.layout.home_list_item, null);
            ImageView ivItem = (ImageView) view.findViewById(R.id.iv_item);
            TextView tvItem = (TextView) view.findViewById(R.id.tv_item);

            tvItem.setText(mItems[position]);
            ivItem.setImageResource(mPics[position]);
            return view;
        }

    }
}
