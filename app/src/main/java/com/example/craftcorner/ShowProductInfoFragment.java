package com.example.craftcorner;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.craftcorner.Client.OrderActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;


public class ShowProductInfoFragment extends DialogFragment {


    String productID, tailorID;
    MaterialTextView productPrice,productStatus,productTitle,productRating,productDesc,productCategory;
    MaterialButton orderButton, contactButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_show_product_info, container, false);

        ImageView productImage=view.findViewById(R.id.product_image);

        productTitle=view.findViewById(R.id.product_title);
        productPrice=view.findViewById(R.id.product_price);
        productStatus=view.findViewById(R.id.product_status);
        productRating=view.findViewById(R.id.product_rating);
        productCategory=view.findViewById(R.id.product_category);
        productDesc=view.findViewById(R.id.product_description);

        orderButton=view.findViewById(R.id.product_order_button);
        contactButton=view.findViewById(R.id.contactToTailor);


        getParentFragmentManager().setFragmentResultListener("ProductKey", getViewLifecycleOwner(), new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
               productID=result.getString("productId");

                DatabaseReference reference= FirebaseDatabase.getInstance().getReference("CraftCorner_Products");

                reference.addValueEventListener(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snap:snapshot.getChildren()) {
                            if (Objects.equals(productID,snap.child("Product_ID").getValue(String.class))){
                                if (Objects.equals(FirebaseAuth.getInstance().getCurrentUser().getUid(),snap.child("Product_Tailor_ID").getValue(String.class))){
                                    orderButton.setVisibility(View.GONE);
                                    contactButton.setVisibility(View.GONE);

                                }
                                productTitle.setText(snap.child("Product_Title").getValue(String.class));
                                productPrice.setText("Rs. "+snap.child("Product_Price").getValue(String.class));
                                productStatus.setText(snap.child("Product_Status").getValue(String.class));
                                productRating.setText("Rating "+snap.child("Product_Rating").getValue(String.class));
                                productCategory.setText(snap.child("Product_Type").getValue(String.class)+" #"+snap.child("Product_Category").getValue(String.class));
                                productDesc.setText(snap.child("Product_Description").getValue(String.class));
                                tailorID=snap.child("Product_Tailor_ID").getValue(String.class);
                                Picasso.get().load(snap.child("Product_ImageUrl").getValue(String.class)).into(productImage);

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });


        contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), MessagingActivity.class);
                intent.putExtra("TailorID",tailorID);
                startActivity(intent);
            }
        });

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), OrderActivity.class);
                intent.putExtra("productID",productID);
                startActivity(intent);
            }
        });


        return view;
    }
}