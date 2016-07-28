package com.chat.mychatapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ChatRoom extends AppCompatActivity {
    //Get Root reference
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mChatRoom = mRootRef.child("chatRoom");

    RecyclerView mRecyclerViewMessage;
    Button mbuttonSendMessage;
    EditText meditTextMessage;
    MessageAdapter mMessageAdapter;
    List<Message> messageList ;

    public static String NAME = "name";
    String mUserName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.chatroom_view);
        mRecyclerViewMessage = (RecyclerView) findViewById(R.id.messagerecyclerview);
        mbuttonSendMessage = (Button) findViewById(R.id.buttonSendMessage);
        meditTextMessage = (EditText) findViewById(R.id.editTextMessage);
        mMessageAdapter = new MessageAdapter();
        messageList = new LinkedList<>();

        Intent intent = getIntent();
        mUserName = intent.getStringExtra(NAME);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mRecyclerViewMessage.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerViewMessage.setHasFixedSize(true);
        final DatabaseReference mChat = mChatRoom.child("Rooom1");
        mRecyclerViewMessage.setAdapter(mMessageAdapter);

        mbuttonSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date = new Date();
                Message chatMsg = new Message(mUserName,meditTextMessage.getText().toString());
                mChat.child(date.toString()).setValue(chatMsg);
                meditTextMessage.setText("");
            }
        });

        mChat.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message Msg = dataSnapshot.getValue(Message.class);
                messageList.add(Msg);
                mMessageAdapter.notifyDataSetChanged();
                mRecyclerViewMessage.smoothScrollToPosition(messageList.size()-1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{

        @Override
        public int getItemCount() {
            return messageList.size();
        }

        @Override
        public int getItemViewType(int position) {
            if(messageList.get(position).getName().equals(mUserName))
                return 0;
            else
                return 1;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.msg.setText(messageList.get(position).getMessage());
            holder.name.setText(messageList.get(position).getName());

        }

        @Override
        public MessageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemLayoutView;

            switch (viewType){
                case 0:
                    itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.messageitem_view, null);

                    break;
                case 1:
                    itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.messageitem_other_view, null);
                    break;
                default:
                    itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.messageitem_view, null);
            }
            ViewHolder viewHolder = new ViewHolder(itemLayoutView);

            return viewHolder;
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            TextView msg;
            TextView name;
            public ViewHolder(View itemView) {
                super(itemView);
                msg = (TextView)itemView.findViewById(R.id.textViewMessage);
                name = (TextView)itemView.findViewById(R.id.textViewName);

            }

            @Override
            public void onClick(View view) {

            }
        }
    }
}
