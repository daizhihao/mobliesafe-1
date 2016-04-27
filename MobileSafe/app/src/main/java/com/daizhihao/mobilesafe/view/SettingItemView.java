package com.daizhihao.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daizhihao.mobilesafe.R;

/**
 * 自定义的组合控件
 * Created by Administrator on 2016/4/26.
 */
public class SettingItemView extends RelativeLayout {
    private static final String NAMESPACE = "http://schemas.android.com/apk/com.daizhihao.mobilesafe";
    private TextView tvTitle;
    private TextView tvDesc;
    private CheckBox cbStatus;
    private String mTitle;
    private String mDescOn;
    private String mDescOff;

    // 用代码new对象时,走此方法
    public SettingItemView(Context context) {
        super(context);
        initView();
    }

    // 有属性时走此方法
    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 根据属性名称,获取属性的值
        mTitle = attrs.getAttributeValue(NAMESPACE,"tv_title");
        mDescOn = attrs.getAttributeValue(NAMESPACE,"desc_on");
        mDescOff = attrs.getAttributeValue(NAMESPACE,"desc_off");
        initView();

    }

    // 有style样式的话会走此方法
    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    /**
     * 初始化布局
     */
    private void initView() {
        // 将自定义好的布局文件设置给当前的SettingItemView
        View.inflate(getContext(), R.layout.view_setting, this);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvDesc = (TextView) findViewById(R.id.tv_desc);
        cbStatus = (CheckBox) findViewById(R.id.cb_status);

        setTitle(mTitle);
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setDesc(String desc) {
        tvDesc.setText(desc);
    }

    /**
     * 返回勾选状态
     *
     * @return
     */
    public boolean isChecked() {
        return cbStatus.isChecked();
    }

    public void setChecked(boolean check) {
        cbStatus.setChecked(check);
        // 根据选择的状态,更新文本描述
        if (check) {
            setDesc(mDescOn);
        } else {
            setDesc(mDescOff);
        }
    }
}
