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

    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            dataSnapshots.add(dataSnapshot);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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
        return dataSnapshot.getValue(InstanceMessage.class);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.chat_msg_row, viewGroup, false);
            final ViewHolder holder = new ViewHolder();
            holder.tvAuthorName = view.findViewById(R.id.author);
            holder.tvMessage = view.findViewById(R.id.message);
            holder.params = (LinearLayout.LayoutParams) holder.tvAuthorName.getLayoutParams();
            view.setTag(holder);

        }

        final InstanceMessage message = getItem(i);
        final ViewHolder holder = (ViewHolder) view.getTag();

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
