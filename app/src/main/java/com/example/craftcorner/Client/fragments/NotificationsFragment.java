package com.example.craftcorner.Client.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.craftcorner.NotificationAdapter;
import com.example.craftcorner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;


public class NotificationsFragment extends Fragment {



    RecyclerView notificationRCView;
    ArrayList<String> title,body;
    NotificationAdapter notificationAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_notifications, container, false);


        notificationRCView=view.findViewById(R.id.notification_recycleView);
        notificationRCView.setLayoutManager(new LinearLayoutManager(getContext()));


        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Notification");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    title=new ArrayList<>();
                    body=new ArrayList<>();

                    for (DataSnapshot dataSnapshot:snapshot.getChildren()) {

                        if (Objects.equals(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid(),dataSnapshot.child("Receiver").getValue().toString())) {
                            body.add(dataSnapshot.child("body").getValue().toString());
                            title.add(dataSnapshot.child("title").getValue().toString());

                        }


                    }
                    notificationAdapter=new NotificationAdapter(title,body);
                    notificationRCView.setAdapter(notificationAdapter);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }
}