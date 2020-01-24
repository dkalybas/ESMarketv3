package com.example.esmarket.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.esmarket.Common.Common;
import com.example.esmarket.Interface.ItemClickListener;
import com.example.esmarket.R;

import androidx.recyclerview.widget.RecyclerView;

 public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener
{

    public TextView txt_cart_name,txt_price,txt_quant;
    public ImageView cart_image;
    public ElegantNumberButton btn_quantity;

    public RelativeLayout view_background;
    public LinearLayout view_foreground;

    private ItemClickListener itemClickListener;

    private void setTxt_cart_name(TextView txt_cart_name){

        this.txt_cart_name= txt_cart_name;
    }



    public CartViewHolder(View itemView) {
        super(itemView);


        txt_cart_name = (TextView)itemView.findViewById(R.id.cart_item_name);
        txt_price = (TextView)itemView.findViewById(R.id.cart_item_Price);
        cart_image = (ImageView)itemView.findViewById(R.id.cart_image);
        btn_quantity = (ElegantNumberButton)itemView.findViewById(R.id.btn_quantity);




        view_background=(RelativeLayout)itemView.findViewById(R.id.view_background);
        view_foreground = (LinearLayout)itemView.findViewById(R.id.view_foreground);




        itemView.setOnCreateContextMenuListener(this);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        menu.setHeaderTitle("Select Action ");
        menu.add(0,0,getAdapterPosition(), Common.DELETE);


    }
}

