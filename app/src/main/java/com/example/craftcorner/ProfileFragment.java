package com.example.craftcorner;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.craftcorner.Tailor.RegistrationFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class ProfileFragment extends DialogFragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root= inflater.inflate(R.layout.fragment_profile, container, false);

        ImageView profile_image=root.findViewById(R.id.user_profile_picture);
        MaterialTextView name=root.findViewById(R.id.name);
        MaterialTextView userName=root.findViewById(R.id.user_name);
        NavigationView navigationView=root.findViewById(R.id.navigation_view);


        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("CraftCorner_User").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.hasChildren()){
                    Picasso.get().load(snapshot.child("ImageUrl").getValue(String.class)).into(profile_image);
                    name.setText(snapshot.child("Name").getValue(String.class));
                    userName.setText(snapshot.child("UserName").getValue(String.class));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.my_profile:{
                        startActivity(new Intent(getContext(),EditProfileActivity.class));

                    }
                    break;
                    case R.id.my_location:{

                        //ProfileFragment.this.dismiss();

                       new MapsFragment().show(getParentFragmentManager(),"Maps");
                    }
                    break;
                    case R.id.accountSignOut:{
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getActivity(), GetStartedActivity.class));
                        getActivity().finish();
                    }
                    break;
                }
                return true;
            }
        });


        return root;
    }
}