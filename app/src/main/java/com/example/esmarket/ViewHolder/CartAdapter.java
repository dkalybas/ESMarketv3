package com.example.esmarket.ViewHolder;


import android.content.Context;
import android.graphics.Color;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.esmarket.Cart;
import com.example.esmarket.Common.Common;
import com.example.esmarket.Database.Database;
import com.example.esmarket.Interface.ItemClickListener;
import com.example.esmarket.Model.Order;
import com.example.esmarket.R;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {

    private List<Order> listData = new ArrayList<>();
    private Context context ;
    private Cart cart ;

    public CartAdapter(List<Order> listData, Cart cart) {
        this.listData = listData;
        this.cart = cart;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater= LayoutInflater.from(cart);
        View itemView = inflater.inflate(R.layout.cart_layout,parent,false);
        return  new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartViewHolder holder, final int position) {

        Picasso.with(cart.getBaseContext())
                .load(listData.get(position).getImage())
                .resize(70,70)
                .centerCrop()
                .into(holder.cart_image);





        holder.btn_quantity.setNumber(listData.get(position).getQuantity());

        holder.btn_quantity.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {


                @Override
                public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {

                    Order order = listData.get(position);
                    order.setQuantity(String.valueOf(newValue));

                    new Database(cart).updateCart(order);


                    //Update of Total

                    int total=0;
                    List<Order> orders = new Database(cart).getCarts(Common.currentUser.getPhone());
                    for(Order item :orders )
                        total +=(Integer.parseInt(item.getPrice()))*(Integer.parseInt(item.getQuantity()));




                    Locale locale  = new Locale("en","US");
                    NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

                    cart.txtTotalPrice.setText(fmt.format(total));






                }

        });

        Locale locale  = new Locale("en","US");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);


        int price = (Integer.parseInt(listData.get(position).getPrice()))*(Integer.parseInt(listData.get(position).getQuantity()));

        holder.txt_price.setText(fmt.format(price));
        holder.txt_cart_name.setText(listData.get(position).getProductName());

    }




    @Override
    public int getItemCount() {
        return listData.size();
    }


    public void  removeItem(int position){

        listData.remove(position)
        ;
        notifyItemRemoved(position);

    }

    public void restoreItem(Order item , int position){


        listData.add(position,item);
        notifyItemInserted(position);


    }

    public Order getItem(int position ){


        return  listData.get(position);

    }




}
