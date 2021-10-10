package com.example.craftcorner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.intellij.lang.annotations.JdkConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class MessagingActivity extends AppCompatActivity {

    ImageView profileImage;
    TextView profileName;
    TextInputEditText text;
    TextInputLayout inputLayout;
    RecyclerView recyclerView;

    ArrayList<String> sender, receiver, message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        profileImage=findViewById(R.id.profileImage);
        profileName=findViewById(R.id.profileName);
        inputLayout=findViewById(R.id.edit_text);
        text=findViewById(R.id.user_text);
        recyclerView=findViewById(R.id.recycleView);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        
        String tailorID=getIntent().getStringExtra("TailorID");

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("CraftCorner_User").child(tailorID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.hasChildren()){
                    Picasso.get().load(snapshot.child("ImageUrl").getValue(String.class)).into(profileImage);
                    profileName.setText(snapshot.child("UserName").getValue(String.class));
                    
                    //read message
                    String userID= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                    readMessage(userID,tailorID);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        inputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message= Objects.requireNonNull(text.getText()).toString();
                if (message.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Write something", Toast.LENGTH_SHORT).show();
                }else {
                    String userID= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                    text.setText("");
                    sendMessage(userID,tailorID,message);
                }
            }
        });

        seenMessages(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid(),tailorID);
    }

    private void readMessage(String userID, String tailorID) {
        sender=new ArrayList<>();
        receiver=new ArrayList<>();
        message=new ArrayList<>();
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean seen = false;
                for (DataSnapshot snap:snapshot.getChildren()) {
                    seen = false;
                    if (Objects.equals(userID,snap.child("Receiver").getValue(String.class)) &&
                            Objects.equals(tailorID,snap.child("Sender").getValue(String.class))

                            ||
                            Objects.equals(tailorID,snap.child("Receiver").getValue(String.class)) &&
                                    Objects.equals(userID,snap.child("Sender").getValue(String.class))){

                        sender.add(snap.child("Sender").getValue(String.class));
                        receiver.add(snap.child("Receiver").getValue(String.class));
                        message.add(snap.child("Message").getValue(String.class));
                        seen=snap.child("isSeen").getValue(Boolean.class);

                    }

                }
                MessageAdapter messageAdapter=new MessageAdapter(MessagingActivity.this,sender,receiver,message,seen);
                recyclerView.setAdapter(messageAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void sendMessage(String userID, String tailorID, String message) {

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Chats");
        try {
            HashMap<String, Object> hashMap=new HashMap<>();
            hashMap.put("Sender",userID);
            hashMap.put("Receiver",tailorID);
            hashMap.put("Message",message);
            hashMap.put("isSeen",false);
            reference.push().setValue(hashMap);

            DatabaseReference chatRef=FirebaseDatabase.getInstance().getReference("ChatList").child(userID).child(tailorID);
            chatRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()){
                        chatRef.child("id").setValue(tailorID);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }catch (Exception e){
            Toast.makeText(MessagingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }





    }

    public void seenMessages(String user, String tailor){
       DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    String send=snapshot.child("Sender").getValue(String.class);
                    String receive=snapshot.child("Receiver").getValue(String.class);
                    if (Objects.equals(user,receive) &&Objects.equals(tailor,send)){
                        HashMap<String, Object> hashMap=new HashMap<>();
                        hashMap.put("isSeen",true);
                        snapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}