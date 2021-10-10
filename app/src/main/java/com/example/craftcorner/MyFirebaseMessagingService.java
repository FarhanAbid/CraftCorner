package com.example.craftcorner;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.craftcorner.Client.ClientHomeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;


public class MyFirebaseMessagingService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        generateMessage(remoteMessage.getNotification().getBody(),remoteMessage.getNotification().getTitle(),remoteMessage.getMessageId());
    }

    private void generateMessage(String body, String title,String Id) {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference();

        HashMap<String,Object> hashMap=new HashMap<>();

        hashMap.put("title",title);
        hashMap.put("body",body);
        hashMap.put("ID",Id);
        hashMap.put("Receiver",FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.child("Notification").push().setValue(hashMap);


        Intent intent=new Intent(this, ClientHomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notification=new NotificationCompat.Builder(this,"0")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(uri)
                .setContentIntent(pendingIntent);
        NotificationManager manager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel=new NotificationChannel("0","Channel_1",NotificationManager.IMPORTANCE_HIGH);

            assert manager != null;
            manager.createNotificationChannel(channel);
        }


        assert manager != null;
        manager.notify(1,notification.build());

    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }
}
