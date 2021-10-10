package com.example.craftcorner.Client.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.craftcorner.ChatsAdapter;
import com.example.craftcorner.MessagingActivity;
import com.example.craftcorner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;


public class MessagesFragment extends DialogFragment {


    ArrayList<String> usersList, imageUrl, name;
    ListView chatsList;
    String sID="", rID="";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root= inflater.inflate(R.layout.fragment_messages, container, false);
        chatsList=root.findViewById(R.id.chatsList);
      DatabaseReference reference= FirebaseDatabase.getInstance().getReference("ChatList").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
      usersList=new ArrayList<>();
      //client tailor receiver list
       reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String chatList=snapshot.child("id").getValue(String.class);
                    Toast.makeText(getContext(), chatList, Toast.LENGTH_SHORT).show();
                    usersList.add(chatList);




                }
                chat_list();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //tailor sender list
      /* DatabaseReference senderReference=FirebaseDatabase.getInstance().getReference("Chats");
        senderReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap:snapshot.getChildren()) {
                    if (Objects.equals(FirebaseAuth.getInstance().getCurrentUser().getUid(),snap.child("Receiver").getValue(String.class))){
                        usersList.add(snap.child("Sender").getValue(String.class));
                    }
                    chat_list(usersList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/



        chatsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getContext(), MessagingActivity.class);
                intent.putExtra("TailorID",usersList.get(i).toString());
                startActivity(intent);
            }
        });


        return root;
    }

    private void chat_list() {

        imageUrl=new ArrayList<>();
        name=new ArrayList<>();

            DatabaseReference reference=FirebaseDatabase.getInstance().getReference("CraftCorner_User");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    imageUrl.clear(); name.clear();
                    for (DataSnapshot snap: snapshot.getChildren()) {
                       String url= snap.child("ImageUrl").getValue(String.class);
                       String username= snap.child("Name").getValue(String.class);
                        String userID= snap.child("UserID").getValue(String.class);
                        for (String id:usersList) {
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