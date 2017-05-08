package me.eagzzycsl.intertent.frag;


import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;

import me.eagzzycsl.intertent.R;
import me.eagzzycsl.intertent.adapter.ChatRecAdapter;
import me.eagzzycsl.intertent.model.ChatMsg;
import me.eagzzycsl.intertent.model.MsgType;
import me.eagzzycsl.intertent.model.SourceType;
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        SQLMan.getInstance(getActivity().getApplicationContext()).addChatHis(
                new ChatMsg(
                        (new GregorianCalendar(2017,4,21,15,40,30)).getTimeInMillis(),
                        MsgType.text,
                        "hi hi hi ",
                        SourceType.pc
                )
        );
        SQLMan.getInstance(getActivity().getApplicationContext()).addChatHis(
                new ChatMsg(
                        (new GregorianCalendar(2017,4,21,15,20,30)).getTimeInMillis(),
                        MsgType.text,
                        "hello world",
                        SourceType.android
                )
        );
        chatRecAdapter.setData(SQLMan.getInstance(getActivity().getApplicationContext()).getAllChatHis());


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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }
    private void toggleSendState(boolean send){
        chat_input_image.setVisibility(send?View.GONE:View.VISIBLE);
        chat_input_file.setVisibility(send?View.GONE:View.VISIBLE);
        chat_input_send.setVisibility(send?View.VISIBLE:View.GONE);
    }
}
