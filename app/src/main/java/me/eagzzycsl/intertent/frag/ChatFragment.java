package me.eagzzycsl.intertent.frag;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.URISyntaxException;
import java.util.ArrayList;

import me.eagzzycsl.intertent.R;
import me.eagzzycsl.intertent.adapter.ChatRecAdapter;
import me.eagzzycsl.intertent.event.MsgEvent;
import me.eagzzycsl.intertent.manager.ServerManager;
import me.eagzzycsl.intertent.model.ChatMsg;
import me.eagzzycsl.intertent.utils.SQLMan;

import static android.app.Activity.RESULT_OK;

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
    private interface Intent_Code{
         int FILE_SELECT_CODE = 0;
        int PICK_IMAGE_REQUEST=1;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        Fresco.initialize(this.getContext());
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
                sendMsg(chatMsg);
                chat_input_edit.setText("");
                break;
            }
            case R.id.chat_input_file:{
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    try {
                        startActivityForResult(
                                Intent.createChooser(intent, "Select a File to Upload"),
                                Intent_Code.FILE_SELECT_CODE);
                    } catch (android.content.ActivityNotFoundException ex) {
                        // Potentially direct the user to the Market with a Dialog
                        Toast.makeText(getContext(), "Please install a File Manager.", Toast.LENGTH_SHORT).show();
                    }
                break;
            }
            case R.id.chat_input_image:{
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(
                        Intent.createChooser(intent, "Select Picture"),
                        Intent_Code.PICK_IMAGE_REQUEST
                );
                break;
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
    private void sendMsg(ChatMsg chatMsg){
        ServerManager.getInstance().sendMsgEvent(chatMsg.toMsgEvent());
        addMsgToDbAndUI(chatMsg);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case Intent_Code.FILE_SELECT_CODE:{
                if(resultCode==RESULT_OK){
                    Uri uri = data.getData();
                    Log.d("filechoose", "File Uri: " + uri.toString());
                    // Get the path
                    String path = null;
                    try {
                        path = getPath(getContext(), uri);
                        Log.d("filechoose", "File Path: " + path);
                        ChatMsg chatMsg =new ChatMsg(
                                System.currentTimeMillis(),
                                ChatMsg.MsgType.type_file,
                                path,
                                ChatMsg.SourceType.type_android
                        );
                        sendMsg(chatMsg);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            case Intent_Code.PICK_IMAGE_REQUEST:{
                if(resultCode==RESULT_OK){
                    Uri uri = data.getData();
                    Log.d("filechoose", "File Uri: " + uri.toString());
                    // Get the path
                    String path = null;
                    try {
                        path = getPath(getContext(), uri);
                        Log.d("filechoose", "File Path: " + path);
                        ChatMsg chatMsg =new ChatMsg(
                                System.currentTimeMillis(),
                                ChatMsg.MsgType.type_img,
                                path,
                                ChatMsg.SourceType.type_android
                        );
                        sendMsg(chatMsg);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
        }
    }
    private String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
                cursor.close();
            } catch (Exception e) {
                // Eat it  Or Log it.
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }
}
