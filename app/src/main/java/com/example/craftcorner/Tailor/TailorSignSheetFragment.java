package com.example.craftcorner.Tailor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.example.craftcorner.GetStartedActivity;
import com.example.craftcorner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;


public class TailorSignSheetFragment extends BottomSheetDialogFragment {

    FirebaseAuth auth;


    TextInputEditText tailor_email, tailor_password;
    MaterialButton tailor_signInButton;
    MaterialTextView forgotPin, forgotPin_text_note, tailor_new_account;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.fragment_tailor_sign_sheet, container, false);

        tailor_signInButton=root.findViewById(R.id.tailor_signIn_button);
        forgotPin=root.findViewById(R.id.tailor_forgotPin);
        tailor_new_account=root.findViewById(R.id.tailor_new_account);

        tailor_email=root.findViewById(R.id.tailor_email_editText);
        tailor_password=root.findViewById(R.id.tailor_password_editText);


        forgotPin_text_note=root.findViewById(R.id.forgotPin_noteText);
        auth=FirebaseAuth.getInstance();

        forgotPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tailor_email.getText().toString().isEmpty()){
                    forgotPin_text_note.setText("Please Enter the Email First!");
                }else {
                   auth.sendPasswordResetEmail(tailor_email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           forgotPin_text_note.setText("");
                           new MaterialAlertDialogBuilder(requireContext()).setTitle("Password Reset Email").setMessage("An Password Reset link has been sent to your Email. Please check your email.").setNeutralButton("Ok",null).show();
                       }
                   }) ;
                }
            }
        });

        tailor_signInButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                String email=tailor_email.getText().toString();
                String password=tailor_password.getText().toString();
                if (isNetworkAvailable()){
                    if (Objects.requireNonNull(email).isEmpty() && Objects.requireNonNull(password).isEmpty()){
                        forgotPin_text_note.setText("Email and Password Required!");
                    }else {
                        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(getContext(), "Account Login Successfully!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getActivity(),TailorHomeActivity.class));
                                    requireActivity().finish();
                                }else Toast.makeText(requireContext(), "Not Found", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                }else {
                    new MaterialAlertDialogBuilder(requireContext()).setTitle("Network Error").setMessage("Your device may not have any internet Connection.").setNeutralButton("Ok",null).show();
                }

            }
        });

        tailor_new_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TailorSignSheetFragment.this.dismiss();

                FragmentManager fragmentManager = getParentFragmentManager();
                RegistrationFragment newFragment = new RegistrationFragment();

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