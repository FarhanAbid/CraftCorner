package com.example.craftcorner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.WriterException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;


public class OrderAdapter extends ArrayAdapter {


    ArrayList<String> orderID, orderTitle, orderPrice, orderStatus, orderTailorID;


    public OrderAdapter(Context context, ArrayList<String> orderID,ArrayList<String> orderTitle,ArrayList<String> orderPrice,ArrayList<String> orderStatus,ArrayList<String> orderTailorID) {
        super(context, R.layout.order_adapter);

        this.orderTitle=orderTitle;
        this.orderID=orderID;
        this.orderPrice=orderPrice;
        this.orderStatus=orderStatus;
        this.orderTailorID=orderTailorID;

    }

    @SuppressLint({"ViewHolder", "SetTextI18n"})
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView=LayoutInflater.from(parent.getContext()).inflate(R.layout.order_adapter, parent, false);

        MaterialTextView order_id=convertView.findViewById(R.id.order_ID);
        MaterialTextView order_title=convertView.findViewById(R.id.order_title);
        MaterialTextView order_price=convertView.findViewById(R.id.order_price);
        MaterialTextView order_status=convertView.findViewById(R.id.order_status);

        ShapeableImageView order_QRCodeImage=convertView.findViewById(R.id.orderQRCode);
        MaterialButton order_button=convertView.findViewById(R.id.order_update);


        order_id.setText("#ID: "+orderID.get(position));
        order_title.setText(orderTitle.get(position));
        order_price.setText("Rs. "+orderPrice.get(position));
        order_status.setText("Status: "+orderStatus.get(position));
        
        if (Objects.equals(FirebaseAuth.getInstance().getCurrentUser().getUid(),orderTailorID.get(position))){
            order_button.setText("Accept");
        }else {
            order_button.setText("Cancel");
        }

        order_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // DatabaseReference reference= FirebaseDatabase.getInstance().getReference("CraftCorner_Orders");
                if (Objects.equals(FirebaseAuth.getInstance().getCurrentUser().getUid(),orderTailorID.get(position))){
                    order_button.setText("Accepted");
                   

                }else {
                    order_button.setText("Canceled");
                    order_button.setBackgroundColor(Color.GRAY);


                }
            }
        });

        //generating QR code of Order ID
        WindowManager windowManager= (WindowManager) parent.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display=windowManager.getDefaultDisplay();
        Point point=new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;

        // generating dimension from width and height.
        int dimen = Math.min(width, height);
        dimen = dimen * 3 / 4;

        QRGEncoder qrgEncoder=new QRGEncoder(orderID.get(position),null, QRGContents.Type.TEXT,dimen);


        try {
            // getting our qrcode in the form of bitmap.
            Bitmap bitmap = qrgEncoder.encodeAsBitmap();
            order_QRCodeImage.setImageBitmap(bitmap);

        } catch (WriterException e) {
            // this method is called for
            // exception handling.
            Toast.makeText(parent.getContext(), "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return convertView;
    }

    @Override
    public int getCount() {
        return orderID.size();
    }
}