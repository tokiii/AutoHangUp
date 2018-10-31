package com.wikikii.hangup;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_open_accessibility;
    Button btn_open_permission;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_open_accessibility = findViewById(R.id.btn_open_accessibility);
        btn_open_permission = findViewById(R.id.btn_open_permission);
        btn_open_accessibility.setOnClickListener(this);
        btn_open_permission.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_open_accessibility:
                OpenAccessibilitySettingHelper.jumpToSettingPage(this);
                break;

            case R.id.btn_open_permission:
                PermissionUtils.permission(Manifest.permission.CALL_PHONE).callback(new PermissionUtils.SimpleCallback() {
                    @Override
                    public void onGranted() {
                        ToastUtils.showShort("权限开启成功");
                    }

                    @Override
                    public void onDenied() {
                        ToastUtils.showShort("权限开启失败");
                    }
                }).request();
                break;
        }
    }
}
