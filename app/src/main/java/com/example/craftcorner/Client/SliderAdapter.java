package com.example.craftcorner.Client;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.craftcorner.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.smarteist.autoimageslider.SliderViewAdapter;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.Holder> {

    int [] images;

    public SliderAdapter(int[] images){
        this.images=images;
    }


    @Override
    public SliderAdapter.Holder onCreateViewHolder(ViewGroup parent) {

        View root= LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_carousel_items,parent,false);


        return new Holder(root);
    }

    @Override
    public void onBindViewHolder(SliderAdapter.Holder viewHolder, int position) {

        viewHolder.imageView.setImageResource(images[position]);
    }

    @Override
    public int getCount() {
        return images.length;
    }



    static class Holder extends SliderViewAdapter.ViewHolder{
        ShapeableImageView imageView;

        public Holder(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.slider_image);
        }
    }
}
