package me.eagzzycsl.intertent.frag;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import me.eagzzycsl.intertent.R;
import me.eagzzycsl.intertent.adapter.ChatRecAdapter;
import me.eagzzycsl.intertent.event.MsgEvent;
import me.eagzzycsl.intertent.manager.ServerManager;
import me.eagzzycsl.intertent.model.ChatMsg;
import me.eagzzycsl.intertent.utils.SQLMan;

/**
 * Created by eagzzycsl on 4/17/17.
 */

public class ChatFragment extends Fragment implements View.OnClickListener{
    private RecyclerView rec_chat_list;
    private ChatRecAdapter chatRecAdapter;
    private ImageButton chat_input_more;
    private ImageButton chat_input_file;
    private ImageButton chat_input_image;
    private ImageButton chat_input_send;
    private EditText chat_input_edit;
    private ArrayList<ChatMsg> chatMsgList;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.myFind(view);
        this.myCreate();
        this.mySet();
    }
    private void myFind(View view){
        this.rec_chat_list=(RecyclerView)view.findViewById(R.id.rec_chat_list);
        this.chat_input_more=(ImageButton)view.findViewById(R.id.chat_input_more);
        this.chat_input_file=(ImageButton)view.findViewById(R.id.chat_input_file);
        this.chat_input_image=(ImageButton)view.findViewById(R.id.chat_input_image);
        this.chat_input_send=(ImageButton)view.findViewById(R.id.chat_input_send);
        this.chat_input_edit=(EditText)view.findViewById(R.id.chat_input_edit);

    }
    private void myCreate(){
        chatRecAdapter =new ChatRecAdapter();
        chatMsgList=SQLMan.getInstance(getActivity().getApplicationContext()).getAllChatHis();
        chatRecAdapter.setData(chatMsgList);
        rec_chat_list.scrollToPosition(chatMsgList.size());


    }
    private void mySet(){
        rec_chat_list.setLayoutManager(
                new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false)
        );
        rec_chat_list.setAdapter(chatRecAdapter);
        chat_input_more.setOnClickListener(this);
        chat_input_file.setOnClickListener(this);
        chat_input_image.setOnClickListener(this);
        chat_input_send.setOnClickListener(this);
        chat_input_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                toggleSendState(s.length()>0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        chat_input_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.chat_input_send:{
                ChatMsg chatMsg =new ChatMsg(
                        System.currentTimeMillis(),
                        ChatMsg.MsgType.type_text,
                        chat_input_edit.getText().toString(),
                        ChatMsg.SourceType.type_android
                );
                ServerManager.getInstance().sendMsgEvent(chatMsg.toMsgEvent());
                this.addMsgToDbAndUI(chatMsg);
                chat_input_edit.setText("");
            }
        }
    }
    private void toggleSendState(boolean send){
        chat_input_image.setVisibility(send?View.GONE:View.VISIBLE);
        chat_input_file.setVisibility(send?View.GONE:View.VISIBLE);
        chat_input_send.setVisibility(send?View.VISIBLE:View.GONE);
    }
    private void addMsgToDbAndUI(ChatMsg chatMsg){
        chatMsg.setId(SQLMan.getInstance(getContext().getApplicationContext()).addChatHis(chatMsg));
        chatMsgList.add(chatMsg);
        chatRecAdapter.notifyItemInserted(chatMsgList.size());
        rec_chat_list.smoothScrollToPosition(chatMsgList.size());
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MsgEvent msgEvent){
        Log.i("msgEvent",msgEvent.toChatMsg().getValue());
        this.addMsgToDbAndUI(msgEvent.toChatMsg());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
