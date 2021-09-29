package com.example.craftcorner.Tailor.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.craftcorner.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Objects;


public class AddNewProductFragment extends DialogFragment {
    
    Uri uri=null;
    ShapeableImageView productImage;
    TextInputEditText productTitle, productPrice, productTime, productDescription;
    AutoCompleteTextView productCategory, productType;
    LinearProgressIndicator indicator;

    int productCount=0;
    String url="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.fragment_addnew_product, container, false);



        MaterialButton create_product=root.findViewById(R.id.upload_product);

        indicator=root.findViewById(R.id.product_upload_indicator);

        productImage=root.findViewById(R.id.product_image);
        productTitle=root.findViewById(R.id.product_title);
        productPrice=root.findViewById(R.id.product_price);
        productTime=root.findViewById(R.id.product_delivery_time);
        productCategory=root.findViewById(R.id.product_category);
        productType=root.findViewById(R.id.product_type);
        productDescription=root.findViewById(R.id.product_description);

        String[] type={"male","female"};
        String[] category={"suit","tShirt","kurta","faraq","kamizShalwar"};
        ArrayAdapter<String> adapterT= new ArrayAdapter<>(requireContext(),android.R.layout.simple_list_item_1,type);
        ArrayAdapter<String> adapterC= new ArrayAdapter<>(requireContext(),android.R.layout.simple_list_item_1,category);
        productType.setAdapter(adapterT);
        productCategory.setAdapter(adapterC);


        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,0);

            }
        });

        create_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title=productTitle.getText().toString();
                String price=productPrice.getText().toString();
                String hours=productTime.getText().toString();
                String description=productDescription.getText().toString();
                String type=productType.getText().toString();
                String category=productCategory.getText().toString();
                if (title.isEmpty() ||
                price.isEmpty() ||
                hours.isEmpty() ||
                description.isEmpty() ||
                type.isEmpty() ||
                category.isEmpty()){
                    Toast.makeText(requireContext(), "All Fields need! Some fields are empty.", Toast.LENGTH_LONG).show();
                }else {
                    indicator.setVisibility(View.VISIBLE);
                    upLoadProduct(title,price,hours,description,type,category);
                }


            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        assert data != null;
        uri=data.getData();
        productImage.setImageURI(uri);

    }



    void upLoadProduct(String title,String price,String time,String desc,String type, String category){


        //store data to new product
        DatabaseReference newProductReference= FirebaseDatabase.getInstance().getReference("CraftCorner_Products");

        HashMap<String, Object> hashMap=new HashMap<>();
        hashMap.put("Product_Title",title);
        hashMap.put("Product_Price",price);
        hashMap.put("Product_DeliveryHours",time);
        hashMap.put("Product_Description",desc);
        hashMap.put("Product_Type",type);
        hashMap.put("Product_Category",category);


        hashMap.put("Product_ID",newProductReference.push().getKey());
        hashMap.put("Product_Tailor_ID", FirebaseAuth.getInstance().getCurrentUser().getUid());

        StorageReference reference= FirebaseStorage.getInstance().getReference("ProductImage_"+newProductReference.push().getKey());

        UploadTask uploadTask=reference.putFile(uri);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                url=reference.getDownloadUrl().toString();
                if (url.isEmpty()){
                    indicator.setVisibility(View.GONE);
                    Toast.makeText(requireContext(), "no image link", Toast.LENGTH_SHORT).show();
                }
                if (!task.isSuccessful()){
                    Toast.makeText(requireContext(), "Uploading Failed", Toast.LENGTH_SHORT).show();
                    throw task.getException();

                }else
                    return reference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()){

                    hashMap.put("Product_ImageUrl",task.getResult().toString());
                    newProductReference.push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            indicator.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "Product Added", Toast.LENGTH_SHORT).show();
                        }
                    });

                }else{
                    url="null";
                    Toast.makeText(requireContext(), "No link is generated yet", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }




}


