package me.eagzzycsl.intertent.frag;

import android.content.Context;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.util.Enumeration;

import me.eagzzycsl.intertent.R;
import me.eagzzycsl.intertent.utils.NetWorkUtils;

public class ConnectFragment extends Fragment implements View.OnClickListener {
    private FloatingActionButton fab_scan_qr;
    private TextView textView_ip;
    public ConnectFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConnectFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConnectFragment newInstance(String param1, String param2) {
        ConnectFragment fragment = new ConnectFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_connect, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.myFindView(view);
        this.myCreate();
        this.mySet();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
    private void myFindView(View mView){
        fab_scan_qr = (FloatingActionButton) mView.findViewById(R.id.fab_scan_qr);
        textView_ip =(TextView)mView.findViewById(R.id.textView_ip);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_scan_qr:{
                break;
            }
        }
    }
    private void mySet(){
        textView_ip.setText(NetWorkUtils.getIp(getContext())+":1995");
        fab_scan_qr.setOnClickListener(this);
    }
    private void myCreate(){
    }
}
