package com.daizhihao.mobilesafe.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.daizhihao.mobilesafe.R;
import com.daizhihao.mobilesafe.utils.StreamUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SplashActivity extends AppCompatActivity {
    protected static final int CODE_UPDATE_DIALOG = 0;
    protected static final int CODE_URL_ERROR = 1;
    protected static final int CODE_NET_ERROR = 2;
    protected static final int CODE_JSON_ERROR = 3;
    protected static final int CODE_ENTER_HOME = 4;// 进入主页面
    private TextView tvVersion;
    private TextView tvProgress;// 下载进度展示
    // 服务器返回的信息
    private String mVersionName;// 版本名
    private int mVersionCode;// 版本号
    private String mDesc;// 版本描述
    private String mDownloadUrl;// 下载地址

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case CODE_UPDATE_DIALOG:
                    showUpdateDailog();
                    break;
                case CODE_URL_ERROR:
//                    Toast.makeText(SplashActivity.this, "url错误", Toast.LENGTH_SHORT)
//                            .show();
                    enterHome();
                    break;
                case CODE_NET_ERROR:
                    Toast.makeText(SplashActivity.this, "网络错误", Toast.LENGTH_SHORT)
                            .show();
                    enterHome();
                    break;
                case CODE_JSON_ERROR:
                    Toast.makeText(SplashActivity.this, "数据解析错误",
                            Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case CODE_ENTER_HOME:
                    enterHome();
                    break;

                default:
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        tvVersion = (TextView) findViewById(R.id.tv_version);
        tvVersion.setText("版本名:" + getVersionName());
        tvProgress = (TextView) findViewById(R.id.tv_progress);// 默认隐藏
        checkVerson();
    }

    //判断是否与服务器版本相同
    private void checkVerson() {
        final long startTime = System.currentTimeMillis();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = mHandler.obtainMessage();
                HttpURLConnection conn = null;
                try {
                    URL url = new URL("www.baidu.com");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");//设置请求方式
                    conn.setConnectTimeout(5000);//设置连接超时
                    conn.setReadTimeout(5000);//设置请求超时
                    int responseCode = conn.getResponseCode();
                    if (responseCode == 200) {
                        FileInputStream fileInputStream = (FileInputStream) conn.getInputStream();
                        //拿到返回的数据
                        String result = StreamUtils.readFromStream(fileInputStream);
                        // 开始解析json
                        JSONObject jo = new JSONObject();
                        mVersionName = jo.getString("versionName");
                        mVersionCode = jo.getInt("versionCode");
                        mDesc = jo.getString("description");
                        mDownloadUrl = jo.getString("downloadUrl");
                        if (mVersionCode > getVersionCode()) {
                            // 服务器的VersionCode大于本地的VersionCode
                            // 说明有更新, 弹出升级对话框
                            msg.what = CODE_UPDATE_DIALOG;
                        } else {
                            // 没有版本更新
                            msg.what = CODE_ENTER_HOME;
                        }
                    }
                } catch (MalformedURLException e) {
                    // url错误的异常
                    msg.what = CODE_URL_ERROR;
                    e.printStackTrace();
                } catch (IOException e) {
                    // 网络错误异常
                    msg.what = CODE_NET_ERROR;
                    e.printStackTrace();
                } catch (JSONException e) {
                    // json解析失败
                    msg.what = CODE_JSON_ERROR;
                    e.printStackTrace();
                } finally {
                    long endTime = System.currentTimeMillis();
                    long timeUsed = endTime - startTime;// 访问网络花费的时间
                    if (timeUsed < 2000) {
                        // 强制休眠一段时间,保证闪屏页展示2秒钟
                        try {
                            Thread.sleep(2000 - timeUsed);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    mHandler.sendMessage(msg);
                    if (conn != null) {
                        conn.disconnect();// 关闭网络连接
                    }
                }
            }
        }).start();
    }

    /**
     * 跳转到主界面
     */
    private void enterHome() {
        Intent intent = new Intent(this,HomeActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 显示升级的信息
     */
    public void showUpdateDailog() {
        AlertDialog.Builder mBulider = new AlertDialog.Builder(this);
        mBulider.setTitle("最新版本:" + mVersionName);
        mBulider.setMessage(mDesc);
        mBulider.setPositiveButton("马上升级", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                download();
            }
        });
        mBulider.setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                enterHome();
            }
        });
        mBulider.show();
    }

    /**
     * 下载apk文件
     */
    private void download() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            tvProgress.setVisibility(View.VISIBLE);// 显示进度
            String target = Environment.getExternalStorageDirectory()+"/updata.apk";
            HttpUtils httpUtils = new HttpUtils();
            httpUtils.download(mDownloadUrl, target, new RequestCallBack<File>() {
                @Override
                // 下载文件的进度
                public void onLoading(long total, long current, boolean isUploading) {
                    super.onLoading(total, current, isUploading);
                    tvProgress.setText("当前进度:"+current*100/total+"%");
                }
                //下载成功
                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    //下载自动跳转到安装界面
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.setDataAndType(Uri.fromFile(responseInfo.result),
                            "application/vnd.android.package-archive");
                    // startActivity(intent);
                    startActivityForResult(intent, 0);// 如果用户取消安装的话,
                    // 会返回结果,回调方法onActivityResult
                }
                //下载失败
                @Override
                public void onFailure(HttpException e, String s) {
                    Toast.makeText(SplashActivity.this, "下载失败!",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            Toast.makeText(SplashActivity.this, "没有找到sdcard",
                    Toast.LENGTH_SHORT).show();
        }
    }
    // 如果用户取消安装的话,回调此方法
    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        enterHome();
        super.onActivityReenter(resultCode, data);
    }

    /**
     * 获取系统版本名
     *
     * @return
     */
    public String getVersionName() {
        PackageManager packageManager = getPackageManager();
        try {
            // 获取包的信息
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            int versionCode = packageInfo.versionCode;
            String versionName = packageInfo.versionName;
            return versionName;
        } catch (PackageManager.NameNotFoundException e) {
            //没有找到包抛出异常
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取系统版本号
     *
     * @return
     */
    public int getVersionCode() {
        PackageManager packageManager = getPackageManager();
        try {
            // 获取包的信息
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            int versionCode = packageInfo.versionCode;
            String versionName = packageInfo.versionName;
            return versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            //没有找到包抛出异常
            e.printStackTrace();
        }
        return -1;
    }
}
