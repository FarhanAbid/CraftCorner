package com.example.craftcorner.Client;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.craftcorner.Client.ui.HomeFragment;
import com.example.craftcorner.Client.ui.NotificationNavigationFragment;
import com.example.craftcorner.Client.ui.OrdersFragment;
import com.example.craftcorner.Client.ui.SearchFragment;
import com.example.craftcorner.ProfileFragment;
import com.example.craftcorner.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;



public class ClientHomeActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_home);


        BottomNavigationView navView = findViewById(R.id.nav_view);

        navView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_profile:{
                        new ProfileFragment().show(getSupportFragmentManager(),"Profile");
                    }
                    break;
                    case R.id.navigation_home:{
                        Fragment fragment=new HomeFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.nav_host_fragment_activity_client_home,fragment)
                                .commit();
                    }
                    break;
                    case R.id.navigation_search:{
                        Fragment fragment=new SearchFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.nav_host_fragment_activity_client_home,fragment)
                                .commit();
                    }
                    break;
                    case R.id.navigation_orders:{
                        Fragment fragment=new OrdersFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.nav_host_fragment_activity_client_home,fragment)
                                .commit();
                    }
                    break;
                    case R.id.navigation_notifications:{
                        Fragment fragment=new NotificationNavigationFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.nav_host_fragment_activity_client_home,fragment)
                                .commit();
                    }

                }
                return true;
            }
        });
    }

}