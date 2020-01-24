package com.example.esmarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.esmarket.Common.Common;
import com.example.esmarket.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignUp extends AppCompatActivity {

    MaterialEditText myPhone ,myName , myPassword,RetypeMyPassword,myHomeAdress;
    Button btnSignUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);



        myPhone = (MaterialEditText)findViewById(R.id.myPhone);
        myPassword = (MaterialEditText)findViewById(R.id.myPassword);
        RetypeMyPassword = (MaterialEditText)findViewById(R.id.retypeMyPassword);
        myName = (MaterialEditText)findViewById(R.id.myName);
        myHomeAdress = (MaterialEditText)findViewById(R.id.myHomeAdress);


        btnSignUp = (Button)findViewById(R.id.btnSignUp);



        //  FireBase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user= database.getReference("User");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Common.isConnectedToInternet(getBaseContext())){


                final ProgressDialog mDialog = new ProgressDialog(SignUp.this);
                mDialog.setMessage("Loading ... ");
                mDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

// Check if   phone already exists

                        if(dataSnapshot.child(myPhone.getText().toString()).exists()){

                            mDialog.dismiss();
                            Toast.makeText(SignUp.this," This phone number is already registered !!! ",Toast.LENGTH_SHORT).show();

                        }else if(!myPassword.getText().toString().equals(RetypeMyPassword.getText().toString())){

                            mDialog.dismiss();
                            Toast.makeText(SignUp.this,"Password doesn't match please retype it  correctly !!! ",Toast.LENGTH_SHORT).show();

                        }else{

                            mDialog.dismiss();
                            User user = new User(myName.getText().toString(),myPassword.getText().toString(),
                                    myHomeAdress.getText().toString(),"-",true,false);
                            table_user.child(myPhone.getText().toString()).setValue(user);
                            Toast.makeText(SignUp.this," Sign up successfully happened  !!! ",Toast.LENGTH_SHORT).show();
                            finish();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });}else{


                    Toast.makeText(SignUp.this,"Please check you internet connection !!! ",Toast.LENGTH_SHORT).show();
                    return ;

                }



            }
        });



    }
}
