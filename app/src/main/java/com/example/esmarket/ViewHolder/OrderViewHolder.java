package com.example.esmarket.ViewHolder;

import android.view.View;
import android.widget.TextView;

import com.example.esmarket.Interface.ItemClickListener;
import com.example.esmarket.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

public TextView txtOrderId,txtOrderStatus,txtOrderPhone,txtOrderAddress;

private ItemClickListener itemClickListener;



            public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            txtOrderAddress = (TextView)itemView.findViewById(R.id.order_address);
            txtOrderId = (TextView)itemView.findViewById(R.id.order_id);
            txtOrderStatus = (TextView)itemView.findViewById(R.id.order_status);
            txtOrderPhone = (TextView)itemView.findViewById(R.id.order_phone);



            itemView.setOnClickListener(this);

            }



                public void setItemClickListener(ItemClickListener itemClickListener) {
                        this.itemClickListener = itemClickListener;
                        }


        @Override
        public void onClick(View v) {

                itemClickListener.onClick(v,getAdapterPosition(),false);


                }






        }