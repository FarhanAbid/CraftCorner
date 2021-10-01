package com.example.craftcorner.Client;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.craftcorner.R;
import com.google.android.material.button.MaterialButton;
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
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class OrderActivity extends AppCompatActivity {


    MaterialTextView productTitle,productPrice,productRating;
    TextInputEditText user_size_width,user_size_length,user_size_shoulder,user_size_arms,user_age,user_size_detail,orderDeliveryTime;
    MaterialButton orderRegister;
    ShapeableImageView clothImage;
    LinearProgressIndicator progressIndicator;

    //data types

    Uri uri=null;

    String productID,tailorID;

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
                        productTitle.setText(snap.child("Product_Title").getValue(String.class));
                        productPrice.setText("Rs. "+snap.child("Product_Price").getValue(String.class));
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
                String size="Width: "+user_size_width.getText().toString()+"\n Length: "+user_size_length.getText().toString()+"\n Shoulder: "+user_size_shoulder.getText().toString()+"\n Arm: "+user_size_arms.getText().toString();
                String otherSizes=user_size_detail.getText().toString();
                String age=user_age.getText().toString();
                String deliveryTime=orderDeliveryTime.getText().toString();

                //condition
                if (deliveryTime.isEmpty() || age.isEmpty() ||
                user_size_width.getText().toString().isEmpty() ||
                user_size_length.getText().toString().isEmpty() ||
                user_size_shoulder.getText().toString().isEmpty() ||
                user_size_arms.getText().toString().isEmpty()){
                    otherSizes="null";
                    Toast.makeText(getApplicationContext(), "Every Field is required!", Toast.LENGTH_SHORT).show();
                }else {
                    //upLoadProduct(title,price,hours,description,type,category);
                }
            }
        });



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
}