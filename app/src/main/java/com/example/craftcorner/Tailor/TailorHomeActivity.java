package com.example.craftcorner.Tailor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.craftcorner.Client.ui.HomeFragment;
import com.example.craftcorner.ProfileFragment;
import com.example.craftcorner.R;
import com.example.craftcorner.Tailor.fragments.ProductsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class TailorHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tailor_home);

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_nav_view);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.navigation_products:{
                        Fragment fragment=new ProductsFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container_view,fragment)
                                .commit();
                    }
                    break;
                    case R.id.navigation_profile:{
                        new ProfileFragment().show(getSupportFragmentManager(),"Profile");
                    }
                    break;
                }

                return true;
            }
        });

    }
}