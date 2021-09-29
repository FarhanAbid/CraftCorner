package com.example.craftcorner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Objects;

public class EditProfileActivity extends AppCompatActivity {



    ShapeableImageView profile_image;
    TextView user_name;
    TextInputEditText name, address, phoneNumber;
    MaterialButton updateProfileButton;
    CircularProgressIndicator progressIndicator;

    AutoCompleteTextView genderView;
    DatabaseReference reference;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        profile_image=findViewById(R.id.profile_image);
        user_name=findViewById(R.id.username);
        name=findViewById(R.id.full_name);
        address=findViewById(R.id.address);
        phoneNumber=findViewById(R.id.phone_number);
        genderView=findViewById(R.id.user_gender);

        progressIndicator=findViewById(R.id.progressIndicator);

        updateProfileButton=findViewById(R.id.update_profile_button);

        user=FirebaseAuth.getInstance().getCurrentUser();
        reference=FirebaseDatabase.getInstance().getReference("CraftCorner_User").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.hasChildren()){

                    Picasso.get().load(snapshot.child("ImageUrl").getValue(String.class)).into(profile_image);
                    user_name.setText(snapshot.child("UserName").getValue(String.class));
                    name.setText(snapshot.child("Name").getValue(String.class));
                    address.setText(snapshot.child("PermanentAddress").getValue(String.class));
                    phoneNumber.setText(snapshot.child("PhoneNumber").getValue(String.class));
                    if (Objects.equals("null", snapshot.child("Gender").getValue(String.class))){
                        Toast.makeText(getApplicationContext(), "No, gender is selected yet!", Toast.LENGTH_SHORT).show();
                    }else genderView.setText( snapshot.child("Gender").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        String[] gender={"male","female"};
        ArrayAdapter<String> adapter= new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,gender);
        genderView.setAdapter(adapter);



        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,0);

            }
        });


        updateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fName=name.getText().toString();
                String pAddress=address.getText().toString();
                String pNumber=phoneNumber.getText().toString();
                String gender=genderView.getText().toString();


                if (fName.isEmpty() ||
                pAddress.isEmpty() ||
                pNumber.isEmpty() ||
                gender.equals("Gender")){
                    new MaterialAlertDialogBuilder(EditProfileActivity.this).setTitle("All Fields Required!").setMessage("A field is empty or you may not have selected your gender yet.").setNeutralButton("ok",null).show();
                }else {
                    progressIndicator.setVisibility(View.VISIBLE);

                    DatabaseReference reference= FirebaseDatabase.getInstance().getReference("CraftCorner_User").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put("Name",fName);
                    hashMap.put("PermanentAddress",pAddress);
                    hashMap.put("PhoneNumber",pNumber);
                    hashMap.put("Gender",gender);
                    reference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressIndicator.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Profile Updated", Toast.LENGTH_SHORT).show();
                        }
                    });



                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && data != null && data.getData() != null) {
            Uri uri = data.getData();
            uploadProfileImage(uri);
        }
}

  void uploadProfileImage(Uri uri){

        //database reference
      DatabaseReference reference=FirebaseDatabase.getInstance().getReference("CraftCorner_User").child(user.getUid());
      HashMap<String, Object> hashMap=new HashMap<>();
      //storage reference
      StorageReference storageReference= FirebaseStorage.getInstance().getReference("ProfileImage_ID_"+user.getUid());

      //uploading image
      progressIndicator.setVisibility(View.VISIBLE);
      UploadTask uploadTask=storageReference.putFile(uri);
      uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
          @Override
          public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

              String url=storageReference.getDownloadUrl().toString();
              if (url.isEmpty()){
                  progressIndicator.setVisibility(View.GONE);
                  Toast.makeText(getApplicationContext(), "no image link", Toast.LENGTH_SHORT).show();
              }
              if (!task.isSuccessful()){
                  Toast.makeText(getApplicationContext(), "Uploading Failed", Toast.LENGTH_SHORT).show();
                  throw task.getException();

              }else return storageReference.getDownloadUrl();
          }
      }).addOnCompleteListener(new OnCompleteListener<Uri>() {
          @Override
          public void onComplete(@NonNull Task<Uri> task) {
              hashMap.put("ImageUrl",task.getResult().toString());
              reference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                  @Override
                  public void onComplete(@NonNull Task<Void> task) {
                      progressIndicator.setVisibility(View.GONE);
                      profile_image.setImageURI(uri);
                      Toast.makeText(getApplicationContext(), "Profile Image Updated", Toast.LENGTH_SHORT).show();
                  }
              });
          }
      });
  }
}