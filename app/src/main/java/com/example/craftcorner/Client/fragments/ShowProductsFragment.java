package com.example.craftcorner.Client.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.example.craftcorner.GridAdapter;
import com.example.craftcorner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;


public class ShowProductsFragment extends DialogFragment {

    ArrayList<String> imageUrl,title;

    String productType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root= inflater.inflate(R.layout.fragment_show_products, container, false);

        GridView gridView=root.findViewById(R.id.products_grid_view);

        getParentFragmentManager().setFragmentResultListener("requestKey", getViewLifecycleOwner(), new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
             productType=result.getString("bundleKey");
            }
        });

        //getting available products from the database
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("CraftCorner_Products");
        if (isNetworkAvailable()){
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    imageUrl=new ArrayList<>();
                    title=new ArrayList<>();
                    for (DataSnapshot snap:snapshot.getChildren()) {
                        if (snap.exists() && snap.hasChildren()){

                              switch (productType){
                                  case "male":{
                                      if (Objects.equals("male",snap.child("Product_Type").getValue(String.class))){
                                          imageUrl.add(snap.child("Product_ImageUrl").getValue(String.class));
                                          title.add(snap.child("Product_Title").getValue(String.class));
                                      }
                                  }
                                  break;
                                  case "female":{
                                      if (Objects.equals("female",snap.child("Product_Type").getValue(String.class))){
                                          imageUrl.add(snap.child("Product_ImageUrl").getValue(String.class));
                                          title.add(snap.child("Product_Title").getValue(String.class));
                                      }
                                  }
                                  break;
                                  case "tShirt":{
                                      if (Objects.equals("tShirt",snap.child("Product_Category").getValue(String.class))){
                                          imageUrl.add(snap.child("Product_ImageUrl").getValue(String.class));
                                          title.add(snap.child("Product_Title").getValue(String.class));
                                      }
                                  }
                                  break;
                              }


                        }else{
                            Toast.makeText(getContext(), "Currently No Products Available!", Toast.LENGTH_LONG).show();
                        }
                    }
                    GridAdapter adapter=new GridAdapter(getContext(),title,imageUrl);
                    gridView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        return root;
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}