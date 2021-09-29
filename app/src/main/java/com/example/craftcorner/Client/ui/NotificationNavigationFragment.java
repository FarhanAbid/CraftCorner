package com.example.craftcorner.Client.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;


import com.example.craftcorner.Client.fragments.MessagesFragment;
import com.example.craftcorner.Client.fragments.NotificationsFragment;
import com.example.craftcorner.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class NotificationNavigationFragment extends Fragment {




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View root =LayoutInflater.from(getContext()).inflate(R.layout.fragment_nav_notifications, container, false);

       TabLayout tableLayout=root.findViewById(R.id.tabLayout);
        ViewPager viewPager=root.findViewById(R.id.tabContainer);
        TabAdapter tabAdapter=new TabAdapter(getParentFragmentManager());
        tabAdapter.addFragments(new MessagesFragment(),"Messages");
        tabAdapter.addFragments(new NotificationsFragment(), "Notifications");
        viewPager.setAdapter(tabAdapter);
       tableLayout.setupWithViewPager(viewPager);


        return root;
    }




    class TabAdapter extends FragmentPagerAdapter{
        ArrayList<Fragment> fragments;
        ArrayList<String> titles;

        public TabAdapter(@NonNull FragmentManager fragmentManager) {
            super(fragmentManager);
            this.fragments=new ArrayList<>();
            this.titles=new ArrayList<>();
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }

        void addFragments(Fragment fragment, String title){
            fragments.add(fragment);
            titles.add(title);
        }



        @Override
        public int getCount() {
            return fragments.size();
        }
    }


}