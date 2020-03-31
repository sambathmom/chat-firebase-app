package com.mona.chatfirebaseapp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class ChatListAdapter extends BaseAdapter {
    private Activity activity;
    private DatabaseReference databaseReference;
    private String displayName;
    private ArrayList<DataSnapshot> dataSnapshots;

    String TAG = "ChatListAdapter";
    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            dataSnapshots.add(dataSnapshot);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            Log.d(TAG, "onChildChanged: "+dataSnapshot.toString());
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    public ChatListAdapter(Activity activity, DatabaseReference databaseReference, String displayName) {
        this.activity = activity;
        this.databaseReference = databaseReference.child("messages");
        databaseReference.addChildEventListener(childEventListener);
        this.displayName = displayName;
        this.dataSnapshots = new ArrayList<>();
    }

    @Override
    public int getCount() {
        Log.d("Chat", "Size " + dataSnapshots.size());
        return dataSnapshots.size();
    }

    @Override
    public InstanceMessage getItem(int i) {
        DataSnapshot dataSnapshot = dataSnapshots.get(i);

        String author = dataSnapshot.child("author").getValue().toString();
        String messge = dataSnapshot.child("message").getValue().toString();
        InstanceMessage instanMessage = new InstanceMessage(messge,author);
        Log.d(TAG, "getItem:"+messge);
        return instanMessage;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder;

        if(view==null){
            holder = new ViewHolder();
            view = LayoutInflater.from(activity).inflate(R.layout.chat_msg_row, viewGroup, false);
            holder.tvAuthorName = view.findViewById(R.id.author);
            holder.tvMessage = view.findViewById(R.id.message);
            holder.params = (LinearLayout.LayoutParams) holder.tvAuthorName.getLayoutParams();
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }


        final InstanceMessage message = getItem(i);
        Log.d(TAG, "getView: "+message.getMessage());
        String author = message.getAuthor();
        holder.tvAuthorName.setText(author);

        String messageText = message.getMessage();
        holder.tvMessage.setText(messageText);

        return view;
    }

    static class ViewHolder {
        TextView tvAuthorName;
        TextView tvMessage;
        LinearLayout.LayoutParams params;
    }

    public void cleanUp() {
        databaseReference.removeEventListener(childEventListener);
    }
}
