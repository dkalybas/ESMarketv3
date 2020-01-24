package com.example.esmarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.esmarket.Common.Common;
import com.example.esmarket.Database.Database;
import com.example.esmarket.Model.Order;
import com.example.esmarket.Model.Product;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProductDetail extends AppCompatActivity {

    TextView product_name,product_price,product_description,product_amount;
    ImageView product_image;
    CollapsingToolbarLayout collapsingToolbarLayout;

    CounterFab btnCart;
    ElegantNumberButton numberButton;

    String productId="";

    FirebaseDatabase database;

    DatabaseReference products;

    Product currentProduct;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        //FireBase

        database = FirebaseDatabase.getInstance();
        products = database.getReference("Products");

        //InitView

        numberButton = (ElegantNumberButton)findViewById(R.id.number_button);
        btnCart = (CounterFab)findViewById(R.id.btnCart);





        btnCart.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View view) {

                new Database(getBaseContext()).addToCart(new Order(
                        Common.currentUser.getPhone(),
                        productId,
                        currentProduct.getName(),
                        numberButton.getNumber(),
                        currentProduct.getPrice(),
                        currentProduct.getAmount(),
                        currentProduct.getImage()



                ));

                Toast.makeText(ProductDetail.this,"Added to Cart ", Toast.LENGTH_SHORT).show();
            }
        });


        btnCart.setCount(new Database(this).getCountCart(Common.currentUser.getPhone()));


        product_amount = (TextView)findViewById(R.id.product_amount);
        product_description = (TextView)findViewById(R.id.product_description);
        product_name = (TextView)findViewById(R.id.product_name);
        product_price = (TextView)findViewById(R.id.product_price);
        product_image = (ImageView) findViewById(R.id.product_image);

        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        //Get Product Id from Intent

        if(getIntent()!=null)
            productId = getIntent().getStringExtra("ProductId");




        if(!productId.isEmpty()){


            if(Common.isConnectedToInternet(getBaseContext())){
                getDetailProduct(productId);
            }
            else{

                Toast.makeText(ProductDetail.this,"Please check you internet connection !!! ",Toast.LENGTH_SHORT).show();
                return ;

            }


        }





    }

    private void getDetailProduct(String productId) {



        products.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentProduct = dataSnapshot.getValue(Product.class);

                //Set Image

                Picasso.with(getBaseContext()).load(currentProduct.getImage()).into(product_image);

                collapsingToolbarLayout.setTitle(currentProduct.getName());

                product_amount.setText(currentProduct.getAmount());

                product_price.setText(currentProduct.getPrice());

                product_name.setText(currentProduct.getName());

                product_description.setText(currentProduct.getDescription());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
