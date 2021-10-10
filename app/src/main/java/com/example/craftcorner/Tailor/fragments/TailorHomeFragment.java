package com.example.craftcorner.Tailor.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.craftcorner.GridAdapter;
import com.example.craftcorner.R;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class TailorHomeFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //
        View root= inflater.inflate(R.layout.fragment_tailor_home, container, false);


        MaterialTextView totalOrders, orderComplete, orderPending;
        totalOrders=root.findViewById(R.id.total_orders);
        orderComplete=root.findViewById(R.id.complete_orders);
        orderPending=root.findViewById(R.id.pending_orders);

        //getting all orders form the data base
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("CraftCorner_Orders");
        if (isNetworkAvailable()){
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int t=0, c=0, p=0;
                    for (DataSnapshot snap:snapshot.getChildren()) {
                        if (snap.exists() && snap.hasChildren()){
                            if (Objects.equals(FirebaseAuth.getInstance().getCurrentUser().getUid(),snap.child("Order_TailorID").getValue(String.class))){

                                t++;
                                if (Objects.equals("pending",snap.child("Order_Status").getValue(String.class))){
                                    p++;
                                } else if (Objects.equals("complete", snap.child("Order_Status").getValue(String.class))) {
                                    c++;
                                }else {
                                   orderComplete.setText("0");
                                   orderPending.setText("0");
                                }

                            }
                        }else{
                            totalOrders.setText("0");
                        }
                    }
                    totalOrders.setText(String.valueOf(t));
                    orderComplete.setText(String.valueOf(c));
                    orderPending.setText(String.valueOf(p));

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