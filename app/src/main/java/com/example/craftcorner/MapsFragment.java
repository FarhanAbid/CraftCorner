package com.example.craftcorner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MapsFragment extends DialogFragment {

    MaterialButton mLocationButton;
    MaterialTextView mLocationTextView;
    LocationManager manager;
    LocationListener listener;
    String result = "null";
    GoogleMap map;


    private final OnMapReadyCallback callback = new OnMapReadyCallback() {


        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            map=googleMap;

            manager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);

            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                new MaterialAlertDialogBuilder(requireContext()).setTitle("Location").setMessage("Please turn on the location for better experience. Thanks!").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                }).setNegativeButton("No",null).create().show();
            }
            listener=new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {

                    LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    map.addMarker(new MarkerOptions().position(myLocation).title("My Location"));
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 10f));
                    Geocoder geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());

                    try {
                        List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                        if (addressList != null && addressList.size() > 0) {
                            Address address = addressList.get(0);
                            StringBuilder stringBuilder = new StringBuilder();
                            for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                                stringBuilder.append(address.getAddressLine(i)).append(" ");
                            }
                            stringBuilder.append(address.getLocality()).append(", ");
                            stringBuilder.append(address.getPostalCode()).append(", ");
                            stringBuilder.append(address.getCountryName());
                            result = stringBuilder.toString();
                            mLocationTextView.setText(result);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            };

            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
            }



            mLocationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //check location permission
                    if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

                    }else {
                        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);

                        //update address in database

                        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("CraftCorner_User").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("PermanentAddress",result);
                        reference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(requireContext(), "Address Updated", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }



                }
            });


        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.fragment_maps, container, false);

        mLocationButton=root.findViewById(R.id.getAddress);
        mLocationTextView=root.findViewById(R.id.yourAddress);





        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        getDialog().getWindow().setLayout(width, height);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==1){
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,listener);
                }
            }
        }
    }
}