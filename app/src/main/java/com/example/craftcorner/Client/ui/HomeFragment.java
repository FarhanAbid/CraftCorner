package com.example.craftcorner.Client.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.helper.widget.Carousel;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import com.example.craftcorner.Client.SliderAdapter;
import com.example.craftcorner.Client.fragments.ShowProductsFragment;
import com.example.craftcorner.GetStartedActivity;
import com.example.craftcorner.R;
import com.example.craftcorner.Tailor.RegistrationFragment;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

public class HomeFragment extends Fragment {




    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View root = LayoutInflater.from(getContext()).inflate(R.layout.fragment_home, container, false);


        SliderView carousel=root.findViewById(R.id.slider_carousel);
        int [] images = {R.drawable.promotion1, R.drawable.promotion2, R.drawable.promotion3};

        SliderAdapter sliderAdapter=new SliderAdapter(images);
        carousel.setSliderAdapter(sliderAdapter);
        carousel.setIndicatorAnimation(IndicatorAnimationType.WORM);
        carousel.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        carousel.startAutoCycle();
        ImageView men,women,kurta,kamizShalwar,tShirt,suit,faraq;

        men=root.findViewById(R.id.men);
        women=root.findViewById(R.id.women);
        kurta=root.findViewById(R.id.kurta);
        kamizShalwar=root.findViewById(R.id.kamizShalwar);
        tShirt=root.findViewById(R.id.tShirt);
        suit=root.findViewById(R.id.dressSuit);
        faraq=root.findViewById(R.id.faraq);


        men.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                FragmentManager fragmentManager =getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.add(android.R.id.content, new ShowProductsFragment())
                        .addToBackStack(null).commit();
                Bundle result = new Bundle();
                result.putString("bundleKey", "male");
                fragmentManager.setFragmentResult("requestKey", result);


            }
        });


        women.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                ShowProductsFragment newFragment = new ShowProductsFragment();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.add(android.R.id.content, newFragment)
                        .addToBackStack(null).commit();
                Bundle result = new Bundle();
                result.putString("bundleKey", "female");
                fragmentManager.setFragmentResult("requestKey", result);

            }
        });
        kurta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager =getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.add(android.R.id.content, new ShowProductsFragment())
                        .addToBackStack(null).commit();
                Bundle result = new Bundle();
                result.putString("bundleKey", "kurta");
                fragmentManager.setFragmentResult("requestKey", result);
            }
        });

        tShirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                ShowProductsFragment newFragment = new ShowProductsFragment();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.add(android.R.id.content, newFragment)
                        .addToBackStack(null).commit();

                Bundle result = new Bundle();
                result.putString("bundleKey", "tShirt");
                fragmentManager.setFragmentResult("requestKey", result);

            }
        });


        kamizShalwar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager =getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.add(android.R.id.content, new ShowProductsFragment())
                        .addToBackStack(null).commit();
                Bundle result = new Bundle();
                result.putString("bundleKey", "kamizShalwar");
                fragmentManager.setFragmentResult("requestKey", result);
            }
        });
        suit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager =getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.add(android.R.id.content, new ShowProductsFragment())
                        .addToBackStack(null).commit();
                Bundle result = new Bundle();
                result.putString("bundleKey", "suit");
                fragmentManager.setFragmentResult("requestKey", result);
            }
        });
        faraq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager =getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.add(android.R.id.content, new ShowProductsFragment())
                        .addToBackStack(null).commit();
                Bundle result = new Bundle();
                result.putString("bundleKey", "faraq");
                fragmentManager.setFragmentResult("requestKey", result);
            }
        });

        return root;
    }


}