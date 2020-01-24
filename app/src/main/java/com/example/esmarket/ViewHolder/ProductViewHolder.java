package com.example.esmarket.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.esmarket.Interface.ItemClickListener;
import com.example.esmarket.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView product_price,product_name;
    public ImageView product_image,quick_cart;
    private ItemClickListener itemClickListener;


    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        product_price=(TextView)itemView.findViewById(R.id.product_price);
        product_name = (TextView)itemView.findViewById(R.id.product_name);
        product_image = (ImageView) itemView.findViewById(R.id.product_image);
        quick_cart = (ImageView)itemView.findViewById(R.id.btn_quick_cart);

        itemView.setOnClickListener(this);

        this.itemClickListener = itemClickListener;


    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(),false);
    }
}
