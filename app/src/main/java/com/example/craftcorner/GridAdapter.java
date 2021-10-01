package com.example.craftcorner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.craftcorner.R;
import com.example.craftcorner.Tailor.RegistrationFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class GridAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> title;
    ArrayList<String> imageUrl;
    ArrayList<String> id;
    FragmentManager manager;

    public GridAdapter(Context context, ArrayList<String> title, ArrayList<String> imageUrl,ArrayList<String> id,FragmentManager fragmentManager){
        this.context=context;
        this.title=title;
        this.imageUrl=imageUrl;
        this.id=id;
        manager=fragmentManager;
    }
    @Override
    public int getCount() {
        return title.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view= LayoutInflater.from(context).inflate(R.layout.tailor_products_grid_adapter,viewGroup,false);

        ImageView imageView=view.findViewById(R.id.tailor_product_image);
        TextView textView=view.findViewById(R.id.tailor_product_title);

        Picasso.get().load(imageUrl.get(i)).into(imageView);
        textView.setText(title.get(i));

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ShowProductInfoFragment newFragment = new ShowProductInfoFragment();

                FragmentTransaction transaction = manager.beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.add(android.R.id.content, newFragment)
                        .addToBackStack(null).commit();
                Bundle bundle=new Bundle();
                bundle.putString("productId",id.get(i));
                manager.setFragmentResult("ProductKey",bundle);
            }
        });


        return view;
    }
}
