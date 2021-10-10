package com.example.craftcorner.Client.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.craftcorner.OrderAdapter;
import com.example.craftcorner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class OrdersFragment extends Fragment {


    ArrayList<String> orderID, orderTitle, orderPrice, orderStatus,orderTailorID;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root=inflater.inflate(R.layout.fragment_orders, container, false);

        ListView orderList=root.findViewById(R.id.orderList);
        TextView orderTextView=root.findViewById(R.id.orderTextView);

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("CraftCorner_Orders");
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists() && snapshot.hasChildren()) {

                    orderID=new ArrayList<>();
                    orderTitle=new ArrayList<>();
                    orderPrice=new ArrayList<>();
                    orderStatus=new ArrayList<>();
                    orderTailorID=new ArrayList<>();

                    for (DataSnapshot snap : snapshot.getChildren()) {
                        if (Objects.equals(FirebaseAuth.getInstance().getCurrentUser().getUid(),snap.child("Order_UserID").getValue(String.class))) {

                            orderID.add(snap.child("Order_ID").getValue(String.class));
                            orderTitle.add(snap.child("Order_Title").getValue(String.class));
                            orderPrice.add(snap.child("Order_Price").getValue(String.class));
                            orderStatus.add(snap.child("Order_Status").getValue(String.class));
                            orderTailorID.add(snap.child("Order_TailorID").getValue(String.class));
                            orderTextView.setText("Product Orders");
                        }else if (Objects.equals(FirebaseAuth.getInstance().getCurrentUser().getUid(),snap.child("Order_TailorID").getValue(String.class))){
                            orderID.add(snap.child("Order_ID").getValue(String.class));
                            orderTitle.add(snap.child("Order_Title").getValue(String.class));
                            orderPrice.add(snap.child("Order_Price").getValue(String.class));
                            orderStatus.add(snap.child("Order_Status").getValue(String.class));
                            orderTailorID.add(snap.child("Order_TailorID").getValue(String.class));
                            orderTextView.setText("Product Orders");
                        }

                    }

                    OrderAdapter orderAdapter =new OrderAdapter(getContext(),orderID,orderTitle,orderPrice,orderStatus,orderTailorID);
                    orderList.setAdapter(orderAdapter);
                }else {
                    Toast.makeText(getContext(), "No Order Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return root;
    }
}