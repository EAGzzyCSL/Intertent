package me.eagzzycsl.intertent.service;

import android.accessibilityservice.AccessibilityService;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import me.eagzzycsl.intertent.R;
import me.eagzzycsl.intertent.event.MouseEvent;
import me.eagzzycsl.intertent.utils.MyLog;

/**
 * Created by eagzzycsl on 4/23/17.
 */

public class ControlService extends   AccessibilityService {
    private View cursorView;
    private WindowManager.LayoutParams cursorLayout;
    private WindowManager windowManager;
    private DisplayMetrics dm;
    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);

        dm =getResources().getDisplayMetrics();

        cursorView = View.inflate(getApplicationContext(), R.layout.cursor, null);
        cursorLayout = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);
        cursorLayout.gravity = Gravity.TOP | Gravity.START;
        cursorLayout.x = 200;
        cursorLayout.y = 200;

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.addView(cursorView, cursorLayout);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MouseEvent mouseEvent){
        switch (mouseEvent.getAction()){
            case MouseEvent.action_move:{
                cursorLayout.x=(int)(mouseEvent.getX()*dm.widthPixels);
                cursorLayout.y=(int)(mouseEvent.getY()*dm.heightPixels);
                windowManager.updateViewLayout(cursorView, cursorLayout);
                break;
            }
            case MouseEvent.action_click:{
                click();
                break;
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

        if (windowManager != null && cursorView != null) {
            windowManager.removeView(cursorView);
        }
    }

    private void click() {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo == null) return;
        AccessibilityNodeInfo nearestNodeToMouse = findSmallestNodeAtPoint(nodeInfo, cursorLayout.x, cursorLayout.y + 50);
        if (nearestNodeToMouse != null) {
            nearestNodeToMouse.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
        nodeInfo.recycle();
    }
    private static AccessibilityNodeInfo findSmallestNodeAtPoint(AccessibilityNodeInfo sourceNode, int x, int y) {
        Rect bounds = new Rect();
        sourceNode.getBoundsInScreen(bounds);

        if (!bounds.contains(x, y)) {
            return null;
        }

        for (int i=0; i<sourceNode.getChildCount(); i++) {
            AccessibilityNodeInfo nearestSmaller = findSmallestNodeAtPoint(sourceNode.getChild(i), x, y);
            if (nearestSmaller != null) {
                return nearestSmaller;
            }
        }
        ActivityManager.RunningAppProcessInfo a=null;
        return sourceNode;
    }
    @Override
    public void onInterrupt() {
    }
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

    }
}
