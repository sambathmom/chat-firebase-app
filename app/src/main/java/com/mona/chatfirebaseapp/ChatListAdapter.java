package com.mona.chatfirebaseapp;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
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
    private String mDisplayName;
    private ArrayList<InstanceMessage> model;

    String TAG = "ChatListAdapter";
    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            Log.d(TAG, "onChildAdded: "+dataSnapshot);

            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                InstanceMessage message = postSnapshot.getValue(InstanceMessage.class);
                Log.d(TAG, "onChildAdded: "+message.getMessage());
                model.add(message);
            }

            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            Log.d(TAG, "onChildChanged: "+dataSnapshot.toString());

            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                InstanceMessage message = postSnapshot.getValue(InstanceMessage.class);
                Log.d(TAG, "onChildChanged: "+message.getMessage());
                model.add(message);
            }

            notifyDataSetChanged();
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
        model = new ArrayList<>();
        this.activity = activity;
        this.databaseReference = databaseReference.child("messages");
        databaseReference.addChildEventListener(childEventListener);

        this.mDisplayName = displayName;
    }

    @Override
    public int getCount() {
        Log.d("Chat", "Size " + model.size());
        return model.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
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
        }else {
            holder = (ViewHolder) view.getTag();
        }

        final InstanceMessage message = model.get(i);

        Boolean isMe = message.getAuthor().equals(mDisplayName);
        setChatRowAppearance(isMe, holder);
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

    public void setChatRowAppearance(boolean isMe, ViewHolder holder) {
        if (isMe) {
            holder.params.gravity = Gravity.END;
            holder.tvAuthorName.setTextColor(Color.GREEN);
            holder.tvMessage.setBackgroundColor(R.drawable.bubble2);
        } else {
            holder.params.gravity = Gravity.START;
            holder.tvAuthorName.setTextColor(Color.BLUE);
            holder.tvMessage.setBackgroundColor(R.drawable.bubble1);
        }

    }
    public void cleanUp() {
        databaseReference.removeEventListener(childEventListener);
    }
}
