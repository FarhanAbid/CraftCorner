package com.example.craftcorner.Tailor.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.craftcorner.GridAdapter;
import com.example.craftcorner.R;
import com.example.craftcorner.Tailor.RegistrationFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;


public class ProductsFragment extends Fragment {

    ArrayList<String> imageUrl,title,productID;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root= inflater.inflate(R.layout.fragment_products, container, false);

        GridView gridView=root.findViewById(R.id.products_grid_view);
        ShapeableImageView noProductImage=root.findViewById(R.id.no_product_image);
        FloatingActionButton newProduct=root.findViewById(R.id.new_product_button);

        //getting available products from the database
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("CraftCorner_Products");
        if (isNetworkAvailable()){
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    imageUrl=new ArrayList<>();
                    title=new ArrayList<>();
                    productID=new ArrayList<>();
                    for (DataSnapshot snap:snapshot.getChildren()) {
                        if (snap.exists() && snap.hasChildren()){
                            if (Objects.equals(snap.child("Product_Tailor_ID").getValue(String.class), FirebaseAuth.getInstance().getCurrentUser().getUid())){

                                imageUrl.add(snap.child("Product_ImageUrl").getValue(String.class));
                                title.add(snap.child("Product_Title").getValue(String.class));
                                productID.add(snap.child("Product_ID").getValue(String.class));

                            }
                        }else{
                            noProductImage.setVisibility(View.VISIBLE);
                        }
                    }
                    GridAdapter adapter=new GridAdapter(getContext(),title,imageUrl,productID, requireActivity().getSupportFragmentManager());
                    gridView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }



        newProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getParentFragmentManager();
                AddNewProductFragment newFragment = new AddNewProductFragment();

                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.add(android.R.id.content, newFragment)
                        .addToBackStack(null).commit();
            }
        });


        return root;
    }




    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}