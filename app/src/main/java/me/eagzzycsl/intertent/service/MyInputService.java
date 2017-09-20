package me.eagzzycsl.intertent.service;

import android.content.Context;
import android.content.Intent;
import android.inputmethodservice.InputMethodService;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteOrder;

import me.eagzzycsl.intertent.R;
import me.eagzzycsl.intertent.SettingSP;
import me.eagzzycsl.intertent.SettingsActivity;
import me.eagzzycsl.intertent.event.InputEvent;
import me.eagzzycsl.intertent.utils.MyLog;

public class MyInputService extends InputMethodService {

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
    }


    @Override
    public void onInitializeInterface() {
        super.onInitializeInterface();
        MyLog.i(MyLog.msg_inputService, "初始化");
    }

    @Override
    public View onCreateInputView() {

        View v = LayoutInflater.from(getApplicationContext()).inflate(
                R.layout.keyboard, null
        );
        return v;
    }

    @Override
    public View onCreateCandidatesView() {
        return null;
    }


    @Override
    public boolean onShowInputRequested(int flags, boolean configChange) {
        return super.onShowInputRequested(flags, configChange);
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(InputEvent inputEvent) {
        InputConnection inputConnection = getCurrentInputConnection();
        if (inputConnection != null) {
            switch (inputEvent.type){
                case InputEvent.type_input:{
                    inputConnection.commitText(inputEvent.value, 0);
                    break;
                }
                case InputEvent.type_enter:{
                    inputConnection.performEditorAction(
                            EditorInfo.IME_ACTION_SEND
                    );
                    break;
                }
                case InputEvent.type_backspace:{
                    inputConnection.deleteSurroundingText(1,0);
                    break;
                }
                case InputEvent.type_cursorLeft:{
                    break;
                }
                case InputEvent.type_cursorRight:{
                    break;
                }
            }
        }
    }
}

