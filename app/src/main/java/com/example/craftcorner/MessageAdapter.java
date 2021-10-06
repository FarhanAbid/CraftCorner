package com.example.craftcorner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;

import java.util.List;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.RecycleViewHolder> {

    private static final int MESSAGE_TYPE_LEFT=0;
    private static final int MESSAGE_TYPE_RIGHT=1;

    private Context context;
    private List<String> sender;
    private List<String> receiver;
    private List<String> message;
    private boolean isSeen;

    private FirebaseUser firebaseUser;

    public MessageAdapter(Context context, List<String> sender, List<String> receiver, List<String> message, boolean isSeen) {
        this.context = context;
        this.sender=sender;
        this.receiver=receiver;
        this.message=message;
        this.isSeen=isSeen;
    }

    @NonNull
    @Override
    public RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType==MESSAGE_TYPE_RIGHT){
            View view= LayoutInflater.from(context).inflate(R.layout.chat_item_right,parent,false);
            return new RecycleViewHolder(view);
        }else {
           View view= LayoutInflater.from(context).inflate(R.layout.chat_item_left,parent,false);
           return new RecycleViewHolder(view);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewHolder holder, final int position) {

        holder.show_message.setText(message.get(position));
        if (position==(message.size()-1)){
            if (isSeen){
                holder.seenImage.setText("seen");
            }else {
                holder.seenImage.setText("delivered");
            }
        }else {
            holder.seenImage.setText("");
        }


    }

    @Override
    public int getItemCount() {
        return message.size();
    }

    public class RecycleViewHolder extends RecyclerView.ViewHolder{

         TextView show_message;
         TextView seenImage;

        RecycleViewHolder(@NonNull View itemView) {
            super(itemView);
           show_message=itemView.findViewById(R.id.text);
           seenImage=itemView.findViewById(R.id.seen);
        }
    }

    @Override
    public int getItemViewType(int position) {

        return MESSAGE_TYPE_RIGHT;

    }

}
