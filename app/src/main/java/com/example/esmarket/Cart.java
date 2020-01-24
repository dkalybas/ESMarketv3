package com.example.esmarket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import info.hoang8f.widget.FButton;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.esmarket.Common.Common;
import com.example.esmarket.Database.Database;
import com.example.esmarket.Helper.RecyclerItemTouchHelper;
import com.example.esmarket.Interface.RecyclerItemTouchHelperListener;
import com.example.esmarket.Model.Order;
import com.example.esmarket.Model.Request;
import com.example.esmarket.ViewHolder.CartAdapter;
import com.example.esmarket.ViewHolder.CartViewHolder;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rey.material.widget.CheckBox;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Cart extends AppCompatActivity implements RecyclerItemTouchHelperListener {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

    FButton btnPlace;
    public TextView txtTotalPrice;
    public String  FinalPrice;
    public Double afterDisc;

    List<Order> cart = new ArrayList<>();
    CartAdapter adapter;
    RelativeLayout rootLayout;
    View order_address_comment ;


    RadioButton radioButtonShipToThisAddress,radioButtonHomeAddress,radioButtonTakeAway;

    Request request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);




        //Firebase

        database=FirebaseDatabase.getInstance();
        requests=database.getReference("Requests");

        //Init

        recyclerView=(RecyclerView)findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        rootLayout = (RelativeLayout)findViewById(R.id.rootLayout);









        //Swipe to delete cart item

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);


        txtTotalPrice =(TextView)findViewById(R.id.txtTotalPrice);
        btnPlace = (FButton)findViewById(R.id.btnPlaceOrder);

        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    //Here i create a new Request

                if(cart.size()>0){
                        showAlertDialog();


                }else{

                    Toast.makeText(Cart.this," Your cart is empty !!!",Toast.LENGTH_SHORT).show();
                }

            }
        });


        loadListProduct();


    }



    private void showAlertDialog() {

        AlertDialog.Builder alertDialog= new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("One more step !!!");
        alertDialog.setMessage("Enter your address :!!! ");

        LayoutInflater inflater = this.getLayoutInflater();
         order_address_comment = inflater.inflate(R.layout.order_address_comment,null);



        final MaterialEditText myComment =(MaterialEditText)order_address_comment.findViewById(R.id.myComment);
        final MaterialEditText otherAddress =(MaterialEditText)order_address_comment.findViewById(R.id.otherAddress);

        radioButtonShipToThisAddress=(RadioButton)order_address_comment.findViewById(R.id.radioButtonShipToThisAddress);
        radioButtonHomeAddress=(RadioButton)order_address_comment.findViewById(R.id.radioButtonShipToHomeAddress);
        radioButtonTakeAway=(RadioButton)order_address_comment.findViewById(R.id.radioButtonTakeAway);




        alertDialog.setView(order_address_comment);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {



                //Discount apo Coupon
            /*
                 if(!Common.currentUser.isCoupon()&&!Common.currentUser.isCouponExchanged())
                 {


                     afterDisc =Double.parseDouble(txtTotalPrice.getText().toString());
                     afterDisc +=  - afterDisc*0.2;


                     Common.currentUser.setCouponExchanged(true);

                     FinalPrice= String.valueOf(afterDisc);


                 }else{


                     FinalPrice=txtTotalPrice.getText().toString();
                 }
*/


                if(radioButtonShipToThisAddress.isChecked()){


                    request = new Request(
                            Common.currentUser.getPhone(),
                            Common.currentUser.getName(),
                            otherAddress.getText().toString(),
                            txtTotalPrice.getText().toString(),
                            "0",
                            myComment.getText().toString(),
                            cart);

                }

                          if(radioButtonHomeAddress.isChecked()){


                    request = new Request(
                            Common.currentUser.getPhone(),
                            Common.currentUser.getName(),
                            Common.currentUser.getHomeAddress(),
                            txtTotalPrice.getText().toString(),
                            "0",
                            myComment.getText().toString(),
                            cart);

                }

                if(radioButtonTakeAway.isChecked()){



                    request = new Request(
                            Common.currentUser.getPhone(),
                            Common.currentUser.getName(),
                            "---",
                            txtTotalPrice.getText().toString(),
                            "0",
                            myComment.getText().toString(),
                            cart);

                }






                    //Delete cart

                    //Submit to FireBase
                    //System.CurrentMill to key
                    requests.child(String.valueOf(System.currentTimeMillis())).setValue(request);

                    new Database(getBaseContext()).cleanCart(Common.currentUser.getPhone());
                    Toast.makeText(Cart.this, "Thank you , your order has been placed !!! ", Toast.LENGTH_SHORT).show();
                    finish();

                }

             });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }



    private void loadListProduct() {

        cart = new Database(this).getCarts(Common.currentUser.getPhone());
        adapter = new CartAdapter(cart,this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        //Calculate  the total Price

        int total=0;
        for(Order order :cart ){

            total +=(Integer.parseInt(order.getPrice()))*(Integer.parseInt(order.getQuantity()));

            Locale locale  = new Locale("en","US");
            NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

            txtTotalPrice.setText(fmt.format(total));
        }
    }




    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if(item.getTitle().equals(Common.DELETE))
            deleteCart(item.getOrder());


        return true;


    }

    private void deleteCart(int order) {
        //we will remove item at  List<order>  by their position
        cart.remove(order);
        //after that we will delete all old data from sqlite
        new Database(this).cleanCart(Common.currentUser.getPhone());
        //then we update data from list<order to sqlite
        for(Order item:cart){

            new Database(this).addToCart(item);
        }
            loadListProduct();

    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof CartViewHolder) {

            String name = ((CartAdapter) recyclerView.getAdapter()).getItem(viewHolder.getAdapterPosition()).getProductName();

            final Order deleteItem = ((CartAdapter) recyclerView.getAdapter()).getItem(viewHolder.getAdapterPosition());

            final int deleteIndex = viewHolder.getAdapterPosition();

            adapter.removeItem(deleteIndex);
            new Database(getBaseContext()).removeFromCart(deleteItem.getProductId(), Common.currentUser.getPhone());


            //Update Total here !!!

            int total = 0;
            List<Order> orders = new Database(getBaseContext()).getCarts(Common.currentUser.getPhone());
            for (Order item : orders)
                total += (Integer.parseInt(item.getPrice())) * (Integer.parseInt(item.getQuantity()));

            Locale locale = new Locale("en", "US");
            NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

            txtTotalPrice.setText(fmt.format(total));





            Snackbar snackbar = Snackbar.make(rootLayout,name +"  Removed from cart !!! ", Snackbar.LENGTH_LONG);

            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.restoreItem(deleteItem,deleteIndex);
                    new Database(getBaseContext()).addToCart(deleteItem);


                    //Update of Total here !!!

                    int total=0;
                    List<Order> orders = new Database(getBaseContext()).getCarts(Common.currentUser.getPhone());
                    for(Order item :orders )
                        total +=(Integer.parseInt(item.getPrice()))*(Integer.parseInt(item.getQuantity()));

                    Locale locale  = new Locale("en","US");
                    NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

                    txtTotalPrice.setText(fmt.format(total));


                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();


        }


    }
    }



