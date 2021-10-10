package com.example.craftcorner;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {


    List<String> title,body;

    public NotificationAdapter(List<String> title, List<String> body) {
        this.title = title;
        this.body = body;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.notification_adapter,parent,false);

        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {

        holder.textView.setText(title.get(position)+": \n"+body.get(position));

    }

    @Override
    public int getItemCount() {
        return title.size();
    }

    public static class  NotificationViewHolder extends RecyclerView.ViewHolder{

        MaterialTextView textView;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.notificationText);
        }
    }
}
