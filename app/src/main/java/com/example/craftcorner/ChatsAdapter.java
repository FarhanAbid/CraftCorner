package com.example.craftcorner;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ChatsAdapter extends ArrayAdapter {

    ArrayList<String> username, imageUrl;


    public ChatsAdapter(@NonNull Context context, ArrayList<String> name, ArrayList<String> imageUrl) {
        super(context, R.layout.chats_adapter);

        this.username=name;
        this.imageUrl=imageUrl;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView=LayoutInflater.from(parent.getContext()).inflate(R.layout.chats_adapter, parent, false);

        MaterialTextView name=convertView.findViewById(R.id.user_name);
        ShapeableImageView image=convertView.findViewById(R.id.user_image);


        name.setText(username.get(position));
        Picasso.get().load(imageUrl.get(position)).into(image);


        return convertView;
    }

    @Override
    public int getCount() {
        return imageUrl.size();
    }


}