package com.example.esmarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.esmarket.Common.Common;
import com.example.esmarket.Database.Database;
import com.example.esmarket.Interface.ItemClickListener;
import com.example.esmarket.Model.Order;
import com.example.esmarket.Model.Product;
import com.example.esmarket.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class ProductsList extends AppCompatActivity {


    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    //FirebaseRecyclerAdapter adapter;

    FirebaseDatabase database ;
    DatabaseReference  productsList;

    String categoryId="";


    FirebaseRecyclerAdapter<Product,ProductViewHolder> adapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_list);


        //Initialization Firebase


        database = FirebaseDatabase.getInstance();
        productsList = database.getReference("Products");

        recyclerView = (RecyclerView)findViewById(R.id.recycler_product);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        //Get Intent here


        if (getIntent() != null){
            categoryId = getIntent().getStringExtra("CategoryId");

        }


        if (!categoryId.isEmpty() && categoryId != null){


             if(Common.isConnectedToInternet(getBaseContext())){
                loadListProduct(categoryId);
                } else{

                Toast.makeText(ProductsList.this,"Please check you internet connection !!! ",Toast.LENGTH_SHORT).show();
                return ;

            }


        }
    }







    private void loadListProduct(String categoryId) {

        Query query = productsList.orderByChild("CategoryId").equalTo(categoryId);


        Log.d("TAG",""+adapter);


        FirebaseRecyclerOptions<Product> options =
                new FirebaseRecyclerOptions.Builder<Product>()
                        .setQuery(query, Product.class)
                        .build();
        adapter = new FirebaseRecyclerAdapter<Product, ProductViewHolder>(options) {

            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.product_item, parent, false);

                return new ProductViewHolder(view);

            }
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, final int position, @NonNull final Product model) {

                holder.product_price.setText(model.getPrice()+"€");
                holder.product_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(holder.product_image);

                //quick cart

                holder.quick_cart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        boolean isExists = new Database(getBaseContext()).
                                checkPoductExists(adapter.getRef(position).
                                        getKey(),Common.currentUser.getPhone());

                        if(!isExists) {
                            new Database(getBaseContext()).addToCart(new Order(
                                    Common.currentUser.getPhone(),
                                    adapter.getRef(position).getKey(),
                                    model.getName(),
                                    model.getPrice(),
                                    "1",
                                    model.getAmount(),
                                    model.getImage()

                            ));

                        }else{

                            new Database(getBaseContext()).increaseCart(Common.currentUser.getPhone(),adapter.getRef(position).getKey());
                        }
                        Toast.makeText(ProductsList.this," Added to Cart ", Toast.LENGTH_SHORT).show();


                    }
                });

                final Product local = model;
                Objects.requireNonNull(holder).setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(ProductsList.this, "" +local.getName(), Toast.LENGTH_SHORT).show();

                            Intent productDetail =  new Intent(ProductsList.this,ProductDetail.class);
                            productDetail.putExtra("ProductId",adapter.getRef(position).getKey()); // it sends product id to new activity
                            startActivity(productDetail);




                    }

                });

            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);





    }

    @Override
    protected void onResume() {
        super.onResume();

        if(adapter!=null){

            adapter.startListening();
        }

    }

    @Override
    public void onStart(){
        super.onStart();
        adapter.startListening();
    }


    @Override
    protected void onStop() {
        super.onStop();
        if(adapter !=null)
            adapter.stopListening();

    }


}
