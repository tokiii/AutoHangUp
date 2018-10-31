package com.wikikii.hangup;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.view.accessibility.AccessibilityEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 挂断电话服务
 */
public class HangUpService extends AccessibilityService {


    boolean isFirst = true;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        if (event.getPackageName() == null) {
            return;
        }
        // 此方法是在主线程中回调过来的，所以消息是阻塞执行的
        // 获取包名
        String pkgName = event.getPackageName().toString();
        int eventType = event.getEventType();

        if (pkgName.contains("android.incallui")) {
            AccessibilityLog.printLog("打电话: " + eventType + " 当前打电话ID为: " + event.getCurrentItemIndex());
            if (eventType == 2048) {
                if (isFirst) { // 如果是第一次拨打电话，则不挂断
                    isFirst = false;
                    return;
                }
                TelephonyManager telMag = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                Class<TelephonyManager> c = TelephonyManager.class;
                Method mthEndCall = null;
                try {
                    mthEndCall = c.getDeclaredMethod("getITelephony", (Class[]) null);
                    mthEndCall.setAccessible(true);
                    final Object obj = mthEndCall.invoke(telMag, (Object[]) null);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            try {
                                // 延迟5秒后自动挂断电话
                                // 再通过ITelephony对象去反射里面的endCall方法，挂断电话
                                Method mt = obj.getClass().getMethod("endCall");
                                //允许访问私有方法
                                mt.setAccessible(true);
                                mt.invoke(obj);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, 500);

                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }

        }

        // AccessibilityOperator封装了辅助功能的界面查找与模拟点击事件等操作
        AccessibilityOperator.getInstance().updateEvent(this, event);
        AccessibilityLog.printLog("eventType: " + eventType + " pkgName: " + pkgName);
        switch (eventType) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                break;

        }
    }

    @Override
    public void onInterrupt() {

    }
}
