package com.example.esmarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.esmarket.Common.Common;
import com.example.esmarket.Model.Request;
import com.example.esmarket.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class OrderStatus extends AppCompatActivity {

    public RecyclerView recyclerView ;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;

    FirebaseDatabase database;

    DatabaseReference requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        // FireBase

        database = FirebaseDatabase.getInstance();
        requests=database.getReference("Requests");

        recyclerView = (RecyclerView)findViewById(R.id.listOrders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOrders(Common.currentUser.getPhone());

    }

    private void loadOrders(String phone) {


        Query getOrderByUser = requests.orderByChild("phone").equalTo(phone);

        FirebaseRecyclerOptions<Request> orderOptions =
                new FirebaseRecyclerOptions.Builder<Request>()
                        .setQuery(getOrderByUser, Request.class)
                        .build();

         adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(orderOptions) {

            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder viewHolder, int position, @NonNull Request model) {

                viewHolder.txtOrderId.setText(adapter.getRef(position).getKey());

                viewHolder.txtOrderStatus.setText(convertCodeStatus(model.getStatus()));
                viewHolder.txtOrderAddress.setText(model.getAddress());
                viewHolder.txtOrderPhone.setText(model.getPhone());

            }

            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.order_layout, parent, false);

                return new OrderViewHolder(view);

            }
        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }

    private String convertCodeStatus(String status) {

        if(status.equals("0"))
            return "Placed !!! ";
        else if(status.equals("1"))
            return "On the way ";
        else  return "Already shipped !!! ";

    }


    @Override
    protected void onStop() {


        super.onStop();
        adapter.stopListening();
    }

}