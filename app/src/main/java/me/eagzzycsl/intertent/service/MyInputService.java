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
    private TextView textView_info;
    private String wsLink = null;
    private String state = null;
    private boolean currentShowIsWsLink = true;
    private SettingSP settingSP;
    private int port;

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
    }

    private String getIp() {
        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        int ipAddress = wm.getConnectionInfo().getIpAddress();
        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
            ipAddress = Integer.reverseBytes(ipAddress);
        }
        byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();

        String ipAddressString;
        try {
            ipAddressString = InetAddress.getByAddress(ipByteArray).getHostAddress();
        } catch (UnknownHostException ex) {
            ipAddressString = null;
        }
        return ipAddressString;
    }

    @Override
    public void onInitializeInterface() {
        super.onInitializeInterface();
        MyLog.i(MyLog.msg_inputService, "初始化");
        settingSP = new SettingSP(this);
        state = getString(R.string.waitForConnect);
        port = settingSP.getDefaultPort();
    }

    @Override
    public View onCreateInputView() {

        View v = LayoutInflater.from(getApplicationContext()).inflate(
                R.layout.keyboard, null
        );
        textView_info = (TextView) v.findViewById(R.id.textView_info);
        v.findViewById(R.id.imageView_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyInputService.this, SettingsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        v.findViewById(R.id.imageView_switch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imeManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imeManager != null) {
                    imeManager.showInputMethodPicker();
                }
            }
        });
        wsLink = getIp() + ":" + port;
        textView_info.setText(wsLink);
        textView_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentShowIsWsLink) {
                    textView_info.setText(state);
                } else {
                    textView_info.setText(wsLink);
                }
                currentShowIsWsLink = !currentShowIsWsLink;
            }
        });
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
        MyLog.i(MyLog.msg_inputService, "销毁");

        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(InputEvent inputEvent) {
        MyLog.i("in inputService", inputEvent.value);
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
                    MyLog.i("input","enterevent");
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

