package com.example.craftcorner.Tailor;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;


import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.craftcorner.EditProfileActivity;
import com.example.craftcorner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class RegistrationFragment extends DialogFragment {


    TextInputEditText shopName, email, password, phoneNumber, description;
    MaterialButton register_button;
    LinearProgressIndicator progressIndicator;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_registration, container, false);

        shopName=root.findViewById(R.id.shop_name);
        email=root.findViewById(R.id.authenticate_email);
        password=root.findViewById(R.id.authenticate_password);
        phoneNumber=root.findViewById(R.id.phone_number);
        description=root.findViewById(R.id.small_description);

        progressIndicator=root.findViewById(R.id.progressIndicator);

        register_button=root.findViewById(R.id.register_tailor);

        FirebaseAuth auth=FirebaseAuth.getInstance();

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sName=shopName.getText().toString();
                String mail=email.getText().toString();
                String pass=password.getText().toString();
                String phone=phoneNumber.getText().toString();
                String des=description.getText().toString();

                if (isNetworkAvailable()){
                    if (sName.isEmpty() ||
                            mail.isEmpty() ||
                            pass.isEmpty() ||
                            phone.isEmpty() ||
                            des.isEmpty()
                    ){
                        progressIndicator.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Every Field is required!", Toast.LENGTH_SHORT).show();
                    }else {
                        progressIndicator.setVisibility(View.VISIBLE);


                        auth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(requireActivity(),new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    DatabaseReference reference=FirebaseDatabase.getInstance().getReference("CraftCorner_User").child(auth.getCurrentUser().getUid());
                                    reference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists() && snapshot.hasChildren()){
                                                progressIndicator.setVisibility(View.GONE);
                                                new MaterialAlertDialogBuilder(requireContext()).setTitle("Authentication Error").setMessage("User with this email already exist, please try use another email").setNeutralButton("Ok",null).show();
                                            }else {
                                                progressIndicator.setVisibility(View.VISIBLE);
                                                HashMap<String,Object> hashMap=new HashMap<>();
                                                hashMap.put("UserName",mail);
                                                hashMap.put("Name",sName);
                                                hashMap.put("Email",mail);
                                                hashMap.put("Gender","null");
                                                hashMap.put("PermanentAddress","null");
                                                hashMap.put("ImageUrl","https://drive.google.com/file/d/1HbVgexHG7OJx5wneidHLxb6xWex9DFAg/view");
                                                hashMap.put("Rating","0");
                                                hashMap.put("Password",pass);
                                                hashMap.put("UserID",auth.getCurrentUser().getUid());
                                                hashMap.put("User","tailor");
                                                hashMap.put("PhoneNumber",phone);
                                                hashMap.put("Description",des);
                                                reference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            progressIndicator.setVisibility(View.GONE);
                                                            Toast.makeText(getContext(), "Account is created!", Toast.LENGTH_SHORT).show();
                                                            startActivity(new Intent(getActivity(), EditProfileActivity.class));
                                                        }


                                                    }
                                                });
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            }
                        });
                    }
                }else {
                    new MaterialAlertDialogBuilder(requireContext()).setTitle("Network Error").setMessage("Your device may not have any internet Connection.").setNeutralButton("Ok",null).show();
                }


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