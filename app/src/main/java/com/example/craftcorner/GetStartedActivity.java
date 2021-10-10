package com.example.craftcorner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.craftcorner.Client.ClientHomeActivity;
import com.example.craftcorner.Tailor.TailorHomeActivity;
import com.example.craftcorner.Tailor.TailorSignSheetFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;

public class GetStartedActivity extends AppCompatActivity {

    MaterialButton clientAuthButton, tailorAuthButton;

    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;
    CircularProgressIndicator indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        clientAuthButton=findViewById(R.id.clientAuthButton);
        tailorAuthButton=findViewById(R.id.tailorAuthButton);
        auth=FirebaseAuth.getInstance();
        indicator=findViewById(R.id.indicator);



        // client google login
        clientAuthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable()){

                   //google client account
                    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken("365156783703-dte31p1nhmeh96dle8d19cd213satfft.apps.googleusercontent.com")
                            .requestEmail()
                            .build();

                   GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(GetStartedActivity.this, gso);
                    Intent intent = mGoogleSignInClient.getSignInIntent();
                    indicator.setVisibility(View.VISIBLE);
                    startActivityForResult(intent, 0);

                }else {
                    new MaterialAlertDialogBuilder(GetStartedActivity.this).setTitle("Network").setMessage("Network Not Available Check Internet Connection.").setNeutralButton("Ok", (dialogInterface, i) -> {
                        Intent intent=new Intent(Intent.ACTION_MANAGE_NETWORK_USAGE);
                        startActivity(intent);
                    }).show();
                }


            }
        });

        // tailor
        tailorAuthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable()){
                    TailorSignSheetFragment bottomSheet=new TailorSignSheetFragment();
                    bottomSheet.show(getSupportFragmentManager(),"SignIn");
                }else {
                    new MaterialAlertDialogBuilder(GetStartedActivity.this).setTitle("Network").setMessage("Network Not Available Check Internet Connection.").show();
                }

            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                assert account != null;

                firebaseGoogleClientAuth(account.getIdToken());

            } catch (ApiException e) {
                indicator.setVisibility(View.GONE);
                new MaterialAlertDialogBuilder(GetStartedActivity.this).setTitle("Exception Occur").setMessage(e.getMessage()).show();
            }
        }

    }

    // google account information store in firebase

    public void firebaseGoogleClientAuth(String token){
        if (token==null){
            new MaterialAlertDialogBuilder(getApplicationContext()).setTitle("Error").setMessage("Account Not Fount").show();

        }else {
            auth=FirebaseAuth.getInstance();
            AuthCredential credential= GoogleAuthProvider.getCredential(token,null);
            auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
                        if (acct != null) {
                            String personName = acct.getDisplayName();
                            String personEmail = acct.getEmail();
                            String personId = acct.getId();
                            Uri personPhoto = acct.getPhotoUrl();

                            user=auth.getCurrentUser();
                            reference= FirebaseDatabase.getInstance().getReference("CraftCorner_User").child(user.getUid());
                            reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists() && snapshot.hasChildren()){
                                        indicator.setVisibility(View.GONE);
                                        Toast.makeText(getApplicationContext(), "User: "+personEmail+": Successfully login! ", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), ClientHomeActivity.class));
                                        finish();

                                    }else {
                                        HashMap<String,String> hashMap=new HashMap<>();
                                        hashMap.put("UserName",personEmail);
                                        hashMap.put("Name",personName);
                                        hashMap.put("GoogleID",personId);
                                        hashMap.put("PhoneNumber","null");
                                        hashMap.put("User","client");
                                        hashMap.put("Rating","0");
                                        hashMap.put("Gender","null");
                                        hashMap.put("PermanentAddress","null");
                                        hashMap.put("ImageUrl",personPhoto.toString());
                                        hashMap.put("UserID",user.getUid());
                                        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull @NotNull Task<Void> task) {

                                                Toast.makeText(getApplicationContext(), "Account is Successfully created, Welcome!", Toast.LENGTH_SHORT).show();
                                                indicator.setVisibility(View.GONE);
                                                startActivity(new Intent(getApplicationContext(), EditProfileActivity.class));
                                                finish();
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    indicator.setVisibility(View.GONE);

                                }
                            });


                        } else{
                            Toast.makeText(getApplicationContext(), "Account Not Available", Toast.LENGTH_SHORT).show();
                        }

                    }else {

                    }
                }
            });
        }

    }

    // cehck internet
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (isNetworkAvailable()){
            FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
            if (firebaseUser!=null){
            DatabaseReference reference=FirebaseDatabase.getInstance().getReference("CraftCorner_User").child(firebaseUser.getUid());
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists() && snapshot.hasChildren()){
                        for (DataSnapshot s: snapshot.getChildren()) {
                            if (Objects.equals("tailor",s.child("User").getValue(String.class))){
                                startActivity(new Intent(GetStartedActivity.this, TailorHomeActivity.class));
                                finish();
                            }else if (Objects.equals("client",s.child("User").getValue(String.class))){
                                startActivity(new Intent(GetStartedActivity.this, ClientHomeActivity.class));
                                finish();
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            }else Toast.makeText(getApplicationContext(), "Please Login!", Toast.LENGTH_SHORT).show();

        }else {
            new MaterialAlertDialogBuilder(GetStartedActivity.this).setTitle("Network").setMessage("Network Not Available Check Internet Connection.").show();
        }
    }
}