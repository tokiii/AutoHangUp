package com.wikikii.hangup;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_open_accessibility;
    Button btn_open_permission;
    Button btn_save_time;
    EditText et_time;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_open_accessibility = findViewById(R.id.btn_open_accessibility);
        btn_open_permission = findViewById(R.id.btn_open_permission);
        btn_open_accessibility.setOnClickListener(this);
        btn_open_permission.setOnClickListener(this);
        btn_save_time = findViewById(R.id.btn_save_time);
        btn_save_time.setOnClickListener(this);
        et_time = findViewById(R.id.et_time);
        et_time.setText(SPUtils.getInstance().getInt("time", 1000) + "");
        et_time.setSelection(et_time.getText().toString().length());
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

            case R.id.btn_save_time:
                SPUtils.getInstance().put("time", Integer.valueOf(et_time.getText().toString()));
                ToastUtils.showShort("保存成功,重新开启辅助权限");
                break;
        }
    }
}
