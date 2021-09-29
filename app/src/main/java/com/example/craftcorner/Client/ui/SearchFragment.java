package com.example.craftcorner.Client.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.example.craftcorner.Product;
import com.example.craftcorner.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SearchFragment extends Fragment {

    GridView gridView;
    SearchView searchView;
    String search="";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View root = LayoutInflater.from(getContext()).inflate(R.layout.fragment_search, container, false);

        searchView=root.findViewById(R.id.search_view);
        gridView=root.findViewById(R.id.products_grid_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search=query;
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search=newText;
                return true;
            }
        });


        return root;
    }

    void searchText(String query){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("CraftCorner_Products");
        if (query.isEmpty()){

        }else {
           // FirebaseRecyclerOption<Product>
        }
    }


}