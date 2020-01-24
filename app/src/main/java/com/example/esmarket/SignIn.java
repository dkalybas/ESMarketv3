package com.example.esmarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.esmarket.Common.Common;
import com.example.esmarket.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rey.material.widget.CheckBox;

public class SignIn extends AppCompatActivity {

    EditText myPhone,myPassword ;
    Button btnSignIn;
    CheckBox ckbRemember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        myPhone=(MaterialEditText)findViewById(R.id.myPhone);
        myPassword= (MaterialEditText)findViewById(R.id.myPassword);
        btnSignIn= (Button)findViewById(R.id.btnSignIn);
        ckbRemember=(CheckBox)findViewById(R.id.ckdRemember);

        //Init paper
        Paper.init(this);

        // Firebase

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user= database.getReference("User");


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Common.isConnectedToInternet(getBaseContext())) {
                    //Save user and password
                    if(ckbRemember.isChecked()){

                            Paper.book().write(Common.USER_KEY,myPhone.getText().toString());
                            Paper.book().write(Common.PWD_KEY,myPassword.getText().toString());


                        }



                    final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                mDialog.setMessage("Loading ... ");
                mDialog.show();




                table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                        if (dataSnapshot.child(myPhone.getText().toString()).exists()) {

                        //Gets User's Info here
                        mDialog.dismiss();

                        User user = dataSnapshot.child(myPhone.getText().toString()).getValue(User.class);
                        user.setPhone(myPhone.getText().toString());// set phone

                                        if (user.getPassword().equals(myPassword.getText().toString())) {

                                            Toast.makeText(SignIn.this, "Sing in successful !!! ", Toast.LENGTH_SHORT).show();
                                            Intent homeIntent = new Intent(SignIn.this,Home.class);
                                            Common.currentUser=user;
                                            startActivity(homeIntent);
                                            finish();
                                            table_user.removeEventListener(this);

                                        } else {

                                            Toast.makeText(SignIn.this, "Wrong Password !!! ", Toast.LENGTH_SHORT).show();


                                        }

                    }else{

                            mDialog.dismiss();
                            Toast.makeText(SignIn.this,"User doesn't exist in Database !! ", Toast.LENGTH_SHORT).show();

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }else{

                    Toast.makeText(SignIn.this,"Please check you Internet connection !!! ",Toast.LENGTH_SHORT).show();
                    return ;


                }
            }
        });



    }





}
