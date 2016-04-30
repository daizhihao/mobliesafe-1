package com.daizhihao.mobilesafe.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * 设置引导页的基类, 不需要在清单文件中注册, 因为不需要界面展示
 * Created by Administrator on 2016/4/28.
 */
public abstract class BaseSetupActivity extends AppCompatActivity {
    private GestureDetector mDetector;
    public SharedPreferences mPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPref = getSharedPreferences("config", MODE_PRIVATE);
        mDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            /**
             * 监听手势滑动事件 e1表示滑动的起点,e2表示滑动终点 velocityX表示水平速度 velocityY表示垂直速度
             */
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                // 判断纵向滑动幅度是否过大, 过大的话不允许切换界面
                if (Math.abs(e1.getRawY()-e2.getRawY())>100){
                    return true;
                }
                // 判断滑动是否过慢
                if (Math.abs(velocityX)<100){
                    return true;
                }
                // 向左划, 下一页
                if (e1.getRawX() - e2.getRawX() > 200) {
                    shownextPage();
                }
                // 向右划,上一页
                if (e2.getRawX() - e1.getRawX() > 200) {
                    showPreviousPage();
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }
    //点击按钮显示下一页
    public void next(View view) {
        shownextPage();
    }

    //点击按钮显示下一页
    public void previous(View view) {
        showPreviousPage();
    }

    public abstract void showPreviousPage();

    public abstract void shownextPage() ;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
