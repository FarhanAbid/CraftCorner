package com.example.craftcorner.Client.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.craftcorner.GridAdapter;
import com.example.craftcorner.Product;
import com.example.craftcorner.R;
import com.firebase.ui.database.ClassSnapshotParser;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;


public class SearchFragment extends Fragment {

    RecyclerView gridView;
    SearchView searchView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View root = LayoutInflater.from(getContext()).inflate(R.layout.fragment_search, container, false);

        searchView=root.findViewById(R.id.search_view);
        gridView=root.findViewById(R.id.products_grid_view);

        gridView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchText(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchText(newText);
                return true;
            }
        });


        return root;
    }

    void searchText(String query){
        FirebaseRecyclerOptions<Product> options;
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("CraftCorner_Products");
        if (query.isEmpty()){

            options=new FirebaseRecyclerOptions.Builder<Product>().setQuery(reference, new SnapshotParser<Product>() {
                @NonNull
                @Override
                public Product parseSnapshot(@NonNull DataSnapshot snapshot) {
                    Product product=new Product();
                    product.setProductTitle(snapshot.child("Product_Title").getValue(String.class));
                    product.setProductImageUrl(snapshot.child("Product_ImageUrl").getValue(String.class));
                    return product;
                }
            }).build();

        }else {
           options=new FirebaseRecyclerOptions.Builder<Product>().setQuery(reference.orderByChild("Product_Title").startAt(query).endAt(query + "\uf8ff"), new SnapshotParser<Product>() {
               @NonNull
               @Override
               public Product parseSnapshot(@NonNull DataSnapshot snapshot) {
                   Product product=new Product();
                   product.setProductTitle(snapshot.child("Product_Title").getValue(String.class));
                   product.setProductImageUrl(snapshot.child("Product_ImageUrl").getValue(String.class));
                   return product;
               }
           }).build();
        }
        FirebaseRecyclerAdapter<Product,FindUserViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Product, FindUserViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final FindUserViewHolder holder, @SuppressLint("RecyclerView") final int position, @NonNull final Product model) {
                holder.name.setText(model.getProductTitle());
                Picasso.get().load(model.getProductImageUrl()).into(holder.imageView);

            }

            @NonNull
            @Override
            public FindUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(getContext()).inflate(R.layout.tailor_products_grid_adapter,parent,false);
                return new FindUserViewHolder(view);
            }
        };
        gridView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }


}

class FindUserViewHolder extends RecyclerView.ViewHolder{

    ImageView imageView;
    MaterialTextView name;

    public FindUserViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView=itemView.findViewById(R.id.tailor_product_image);
        name=itemView.findViewById(R.id.tailor_product_title);

    }
}