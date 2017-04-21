package me.eagzzycsl.intertent.adapter;

import android.support.annotation.CallSuper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import me.eagzzycsl.intertent.R;
import me.eagzzycsl.intertent.model.ChatMsg;
import me.eagzzycsl.intertent.model.MsgType;


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
            ((LinearLayout) itemView).setGravity(Gravity.END);
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
        }
    }

    static class ChatImgViewHolder extends ChatRecViewHolder {

        public ChatImgViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        void findView(View itemView) {
        }

        @Override
        public void updateUI(ChatMsg chatMsg) {
            super.updateUI(chatMsg);
        }
    }

    static class ChatFileViewHolder extends ChatRecViewHolder {
        public ChatFileViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        void findView(View itemView) {

        }

        @Override
        public void updateUI(ChatMsg chatMsg) {
            super.updateUI(chatMsg);
        }
    }

    public void setData(ArrayList<ChatMsg> chatHis) {
        this.chatHis = chatHis;
    }

    @Override
    public ChatRecViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case MsgType.type_text: {
                return new ChatTextViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(
                                R.layout.chat_msg_text, parent, false
                        ));
            }
            case MsgType.type_img: {
                return new ChatTextViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(
                                R.layout.chat_msg_img, parent, false
                        ));
            }
            case MsgType.type_file: {
                return new ChatTextViewHolder(
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
        return chatHis.get(position).getTypeInt();
    }
}
