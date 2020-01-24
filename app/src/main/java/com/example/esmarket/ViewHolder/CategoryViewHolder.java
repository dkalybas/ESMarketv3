package com.example.esmarket.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.esmarket.Interface.ItemClickListener;
import com.example.esmarket.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

    public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtCategoryName;
        public ImageView imageView;


        private ItemClickListener itemClickListener;


        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            txtCategoryName = (TextView)itemView.findViewById(R.id.category_name);
            imageView = (ImageView) itemView.findViewById(R.id.category_image);

            itemView.setOnClickListener(this);


            this.itemClickListener = itemClickListener;


        }


        public void setItemClickListener(ItemClickListener itemClickListener) {
            // itemView.SetOnClickListener(this);
            this.itemClickListener= itemClickListener;
        }

        @Override
    public void onClick(View view) {

            itemClickListener.onClick(view, getAdapterPosition(),false);
    }
}
