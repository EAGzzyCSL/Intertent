package me.eagzzycsl.intertent.adapter;

import android.net.Uri;
import android.support.annotation.CallSuper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import me.eagzzycsl.intertent.R;
import me.eagzzycsl.intertent.model.ChatMsg;


public class ChatRecAdapter extends Adapter<ChatRecAdapter.ChatRecViewHolder> {
    private ArrayList<ChatMsg> chatHis;

    static abstract class ChatRecViewHolder extends RecyclerView.ViewHolder {
        private TextView textView_time;

        ChatRecViewHolder(View itemView) {
            super(itemView);
            this.textView_time = (TextView) itemView.findViewById(R.id.rec_chat_list_time);
            findView(itemView);
        }

        abstract void findView(View itemView);

        @CallSuper
        public void updateUI(ChatMsg chatMsg) {

            ((LinearLayout) itemView).setGravity(
                    chatMsg.getSourceType()== ChatMsg.SourceType.type_android?Gravity.END:Gravity.START
            );
        }
    }

    static class ChatTextViewHolder extends ChatRecViewHolder {
        private TextView textView_msgText;

        ChatTextViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        void findView(View itemView) {
            this.textView_msgText = (TextView) itemView.findViewById(R.id.chat_msg_text);
        }

        @Override
        public void updateUI(ChatMsg chatMsg) {
            super.updateUI(chatMsg);
            textView_msgText.setText(chatMsg.getValue());
        }
    }

    static class ChatImgViewHolder extends ChatRecViewHolder {
        private SimpleDraweeView chat_msg_img;
        public ChatImgViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        void findView(View itemView) {
            this.chat_msg_img=(SimpleDraweeView)itemView.findViewById(R.id.chat_msg_img);
        }

        @Override
        public void updateUI(ChatMsg chatMsg) {
            super.updateUI(chatMsg);
            this.chat_msg_img.setImageURI(Uri.fromFile(new File(chatMsg.getValue())));
        }
    }

    static class ChatFileViewHolder extends ChatRecViewHolder {
        private TextView chat_msg_file_name;
        public ChatFileViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        void findView(View itemView) {
            this.chat_msg_file_name=(TextView)itemView.findViewById(R.id.chat_msg_file_name);
        }

        @Override
        public void updateUI(ChatMsg chatMsg) {
            super.updateUI(chatMsg);
            this.chat_msg_file_name.setText(new File(chatMsg.getValue()).getName());
        }
    }

    public void setData(ArrayList<ChatMsg> chatHis) {
        this.chatHis = chatHis;
    }

    @Override
    public ChatRecViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ChatMsg.MsgType.type_text: {
                return new ChatTextViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(
                                R.layout.chat_msg_text, parent, false
                        ));
            }
            case ChatMsg.MsgType.type_img: {
                return new ChatImgViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(
                                R.layout.chat_msg_img, parent, false
                        ));
            }
            case ChatMsg.MsgType.type_file: {
                return new ChatFileViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(
                                R.layout.chat_msg_file, parent, false
                        ));
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ChatRecViewHolder holder, int position) {
        holder.updateUI(chatHis.get(position));
    }

    @Override
    public int getItemCount() {
        return chatHis == null ? 0 : chatHis.size();
    }

    @Override
    public int getItemViewType(int position) {
        return chatHis.get(position).getMsgType();
    }
}
