package me.eagzzycsl.intertent.frag;


import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class ChatFragment extends Fragment {
    private RecyclerView rec_chat_list;
    private ChatRecAdapter chatRecAdapter;

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
    }
}
