package com.example.craftcorner.Client;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.craftcorner.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
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

public class OrderActivity extends AppCompatActivity {


    MaterialTextView productTitle,productPrice,productRating;
    TextInputEditText user_size_width,user_size_length,user_size_shoulder,user_size_arms,user_age,user_size_detail,orderDeliveryTime;
    MaterialButton orderRegister;
    ShapeableImageView clothImage;
    LinearProgressIndicator progressIndicator;

    //data types

    Uri uri=null;

    String productID,tailorID,product_Title,product_Price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        progressIndicator=findViewById(R.id.progressIndicator);

        productTitle=findViewById(R.id.product_title);
        productPrice=findViewById(R.id.product_price);
        productRating=findViewById(R.id.product_rating);

        clothImage=findViewById(R.id.resource_image);

        user_size_width=findViewById(R.id.user_width);
        user_size_length=findViewById(R.id.user_length);

        user_size_shoulder=findViewById(R.id.user_shoulders);
        user_size_arms=findViewById(R.id.user_arms);
        user_size_detail=findViewById(R.id.user_otherSizes);

        user_age=findViewById(R.id.user_age);
        orderDeliveryTime=findViewById(R.id.order_delivery_time);


        orderRegister=findViewById(R.id.order_register);

        productID=getIntent().getStringExtra("productID");
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("CraftCorner_Products");

        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap:snapshot.getChildren()) {
                    if (Objects.equals(productID,snap.child("Product_ID").getValue(String.class))){
                        if (Objects.equals(FirebaseAuth.getInstance().getCurrentUser().getUid(),snap.child("Product_Tailor_ID").getValue(String.class))){
                           // orderButton.setVisibility(View.GONE);
                            //contactButton.setVisibility(View.GONE);

                        }
                        product_Title=snap.child("Product_Title").getValue(String.class);
                        product_Price=snap.child("Product_Price").getValue(String.class);
                        productTitle.setText(product_Title);
                        productPrice.setText("Rs. "+product_Price);
                        productRating.setText(snap.child("Product_Rating").getValue(String.class));
                        tailorID=snap.child("Product_Tailor_ID").getValue(String.class);


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        clothImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,0);
            }
        });


        orderRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String width=user_size_width.getText().toString();
                String length=user_size_length.getText().toString();
                String shoulder=user_size_shoulder.getText().toString();
                String arms=user_size_arms.getText().toString();

                String size="Width: "+width+"\n Length: "+length+"\n Shoulder: "+shoulder+"\n Arm: "+arms;



                String otherSizes=user_size_detail.getText().toString();
                String age=user_age.getText().toString();
                String deliveryTime=orderDeliveryTime.getText().toString();

                //condition
                if (isNetworkAvailable()) {

                    if (deliveryTime.isEmpty() || age.isEmpty() ||
                            width.isEmpty() || length.isEmpty() ||
                            shoulder.isEmpty() || arms.isEmpty()) {

                        Toast.makeText(getApplicationContext(), "Every Field is required!", Toast.LENGTH_SHORT).show();


                    } else {
                        progressIndicator.setVisibility(View.VISIBLE);
                        //extra sizes if empty
                        if (otherSizes.isEmpty()) {
                            otherSizes = "null";
                        }
                        upLoadOrder(size,otherSizes,age,deliveryTime);
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Your device may not have any internet Connection.", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    private void upLoadOrder(String size, String otherSizes, String age, String deliveryTime) {

        if (productID.isEmpty()){
            Toast.makeText(getApplicationContext(), "No, Product found", Toast.LENGTH_SHORT).show();
        }else {
            DatabaseReference reference=FirebaseDatabase.getInstance().getReference("CraftCorner_Orders");

            HashMap<String, Object> hashMap=new HashMap<>();
            hashMap.put("Order_Title",product_Title);
            hashMap.put("Order_Price",product_Price);
            hashMap.put("Order_DeliveryHours",deliveryTime);
            hashMap.put("Order_UserSize",size);
            hashMap.put("Order_UserAge",age);
            hashMap.put("Order_UserSizeDetails",otherSizes);
            hashMap.put("Order_Status","pending");
            hashMap.put("Order_ID",reference.push().getKey());
            hashMap.put("Order_UserID", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
            hashMap.put("Order_TailorID",tailorID);
            hashMap.put("Order_Payment","OnDelivery");

            StorageReference storageReference= FirebaseStorage.getInstance().getReference("OrderImage"+reference.push().getKey());
            if (uri==null){
                Toast.makeText(OrderActivity.this, "Image Uri Not Found, Please again select an image.", Toast.LENGTH_SHORT).show();
            }else {
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
                            throw Objects.requireNonNull(task.getException());

                        }else
                            return storageReference.getDownloadUrl();


                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            hashMap.put("Order_ResourceImageUrl",task.getResult().toString());
                            reference.push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        progressIndicator.setVisibility(View.GONE);
                                        Toast.makeText(getApplicationContext(), "Order request is sent.", Toast.LENGTH_LONG).show();
                                    }

                                }
                            });
                        }
                    }
                });
            }

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==0){
            assert data != null;
            uri=data.getData();
            clothImage.setImageURI(uri);
        }
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}