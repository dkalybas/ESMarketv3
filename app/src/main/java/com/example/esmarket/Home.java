package com.example.esmarket;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.andremion.counterfab.CounterFab;
import com.example.esmarket.Common.Common;
import com.example.esmarket.Database.Database;
import com.example.esmarket.Interface.ItemClickListener;
import com.example.esmarket.Model.Category;
import com.example.esmarket.ViewHolder.CategoryViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;

import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import static com.example.esmarket.Common.Common.currentUser;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;

    FirebaseDatabase database;
    DatabaseReference category;
    TextView txtFullName;

    RecyclerView recycler_category;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter adapter;
    CounterFab fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Categories");
        setSupportActionBar(toolbar);

        //Initialization of FireBase

        database=FirebaseDatabase.getInstance();
        category = database.getReference("Category");


        Paper.init(this);

        
        fab = (CounterFab) findViewById(R.id.fab);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // this is cart intent
                Intent cartIntent = new Intent(Home.this,Cart.class);
                startActivity(cartIntent);



            }
        });

        fab.setCount(new Database(this).getCountCart(Common.currentUser.getPhone()));
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(this);


        //Here I set name for the user

        View headerView = navigationView.getHeaderView(0);
        txtFullName = (TextView)headerView.findViewById(R.id.txtFullName);
        txtFullName.setText(currentUser.getName());

        //Loading of Categories

        //Load Category
        recycler_category = (RecyclerView)findViewById(R.id.recycler_category);
        recycler_category.setHasFixedSize(true);


        recycler_category.setLayoutManager(new GridLayoutManager(this,2));


        if (Common.isConnectedToInternet(this)) {

            loadCategories();
        }else{


            Toast.makeText(this,"Please check you Internet connection !!! ",Toast.LENGTH_SHORT).show();
            return ;

        }




        //onNavigationItemSelected(item);

    }

    private void loadCategories() {

        FirebaseRecyclerOptions<Category> options  =
                new FirebaseRecyclerOptions.Builder<Category>().setQuery(category,Category.class).build();

        adapter = new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(options) {
            @NonNull
            @Override
            public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView;
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item,parent,false);
                return new CategoryViewHolder(itemView);
            }

            @Override
            protected void onBindViewHolder(@NonNull CategoryViewHolder menuViewHolder, int position,
                                            @NonNull Category model) {




                menuViewHolder.txtCategoryName.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(menuViewHolder.imageView);
                final Category clickItem = model ;
                menuViewHolder.setItemClickListener(new ItemClickListener(){

                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {


                        //Get CategoryID and send to new Activity

                       Intent productList = new Intent(Home.this,ProductsList.class);
                        //Because CategoryID  is key , so we just get key of this item .
                        productList.putExtra("CategoryId",adapter.getRef(position).getKey());
                       startActivity(productList);


                    }
                });
            }
        };
        adapter.startListening();
        recycler_category.setAdapter(adapter);
        //swipeRefreshLayout.setRefreshing(false);



    }

    @Override
    protected void onResume() {
        super.onResume();
        fab.setCount(new Database(this).getCountCart(Common.currentUser.getPhone()));

        if(adapter!=null){
            adapter.startListening();
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer =(DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement
        if (item.getItemId()== R.id.refresh)

            loadCategories();


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

         if (id == R.id.nav_cart) {

            Intent cartIntent = new Intent(Home.this,Cart.class);
           startActivity(cartIntent);


        } else if (id == R.id.nav_orders) {

            Intent orderIntent = new Intent(Home.this,OrderStatus.class);
            startActivity(orderIntent);

        } else if(id == R.id.nav_FreeCoupon){

            Intent cartIntent = new Intent(Home.this,RefferalFreeCoupon.class);
            startActivity(cartIntent);


        }else if (id==R.id.nav_newsletter){

            startActivity(new Intent(Home.this,Newsletter.class));

        } else if(id== R.id.nav_change_password){

           showChangepasswordDialog();



        }else if (id == R.id.nav_log_out) {


            //Delete Remember user & password

            Paper.book().destroy();


            // LogOut here
            Intent signIn = new Intent(Home.this,SignUp.class);
            signIn.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(signIn);


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showChangepasswordDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Home.this);
        alertDialog.setTitle("Change Password ");
        alertDialog.setMessage("Please fill information below");

        LayoutInflater inflater = LayoutInflater.from(this);
        View layout_pwd = inflater.inflate(R.layout.change_password_layout,null);

        final MaterialEditText myPassword = (MaterialEditText)layout_pwd.findViewById(R.id.myPassword);
        final MaterialEditText myNewPassword = (MaterialEditText)layout_pwd.findViewById(R.id.myNewPassword);
        final MaterialEditText myRetypeNewPassword = (MaterialEditText)layout_pwd.findViewById(R.id.myRetypeNewPassword);

        alertDialog.setView(layout_pwd);

        //Button
        alertDialog.setPositiveButton("CHANGE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                final android.app.AlertDialog waitingDialog = new SpotsDialog(Home.this);
                waitingDialog.show();

                //Check old password here
                if(myPassword.getText().toString().equals(currentUser.getPassword())){

                    //Check new Password and repeat password

                    if(myNewPassword.getText().toString().equals(myRetypeNewPassword.getText().toString())){

                        Map<String,Object> passwordUpdate = new HashMap<>();
                        passwordUpdate.put("Password" ,myNewPassword.getText().toString());

                        //Make update

                        DatabaseReference user = FirebaseDatabase.getInstance().getReference("User");
                        user.child(currentUser.getPhone())
                                .updateChildren(passwordUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {


                                        waitingDialog.dismiss();;
                                        Toast.makeText(Home.this,"Password was updated",Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Home.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });


                    }else{

                        waitingDialog.dismiss();
                        Toast.makeText(Home.this,"New Password doesn't match ",Toast.LENGTH_SHORT).show();
                    }


                }else {
                    waitingDialog.dismiss();
                    Toast.makeText(Home.this,"Wrong old Password",Toast.LENGTH_SHORT).show();

                }

            }
        });

        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();




    }


}
