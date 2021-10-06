package com.example.craftcorner.Client.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.craftcorner.ChatsAdapter;
import com.example.craftcorner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;


public class MessagesFragment extends Fragment {


    ArrayList<String> usersList, imageUrl, name;
    ListView chatsList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root= inflater.inflate(R.layout.fragment_messages, container, false);
        chatsList=root.findViewById(R.id.chatsList);
      DatabaseReference reference= FirebaseDatabase.getInstance().getReference("ChatList").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
      usersList=new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String chatList=snapshot.child("id").getValue(String.class);
                    usersList.add(chatList);
                }
                chat_list(usersList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return root;
    }

    private void chat_list(ArrayList<String> list) {

            DatabaseReference reference=FirebaseDatabase.getInstance().getReference("CraftCorner_User");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    imageUrl=new ArrayList<>();
                    name=new ArrayList<>();
                    for (DataSnapshot snap: snapshot.getChildren()) {
                       String url= snap.child("ImageUrl").getValue(String.class);
                       String username= snap.child("Name").getValue(String.class);
                        String userID= snap.child("UserID").getValue(String.class);
                        for (String id:list) {
                           if (Objects.equals(id,userID)){
                               imageUrl.add(url);
                               name.add(username);
                           }
                        }

                    }
                    ChatsAdapter chatsAdapter=new ChatsAdapter(requireContext(),name,imageUrl);
                    chatsList.setAdapter(chatsAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


    }
}