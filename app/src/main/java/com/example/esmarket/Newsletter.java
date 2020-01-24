package com.example.esmarket;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.esmarket.Common.Common;
import com.example.esmarket.Model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.regex.Pattern;


public class Newsletter extends AppCompatActivity {




    EditText myEmail;
    Button btnEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsletter);



        myEmail= (MaterialEditText)findViewById(R.id.myEmail);
        btnEmail= (Button) findViewById(R.id.btnEmail);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user= database.getReference("User");



        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





                if(Common.currentUser.getEmail().equals("-")){

                    if (isValid(myEmail.getText().toString())) {

                        final ProgressDialog mDialog1 = new ProgressDialog(Newsletter.this);
                        mDialog1.setMessage("Loading ... ");
                        mDialog1.show();


                        mDialog1.dismiss();
                        Common.currentUser.setEmail(myEmail.getText().toString());
                        User user = new User(Common.currentUser.getName(), Common.currentUser.getPassword()
                                , Common.currentUser.getPhone(), myEmail.getText().toString());
                        table_user.child(Common.currentUser.getPhone()).setValue(user);
                        Toast.makeText(Newsletter.this, "Thanks for subscribing to " +
                                "our Newsletter !!! ", Toast.LENGTH_SHORT).show();
                        finish();

                    } else {


                        Toast.makeText(Newsletter.this, "Invalid Email type please . Please " +
                                "retype  a valid Email address !!!" +
                                " ", Toast.LENGTH_SHORT).show();
                    }


                }else {


                    Toast.makeText(Newsletter.this, " You have already made a subscription !!! ", Toast.LENGTH_SHORT).show();


                }





            }
        });




    }

    public static boolean isValid(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }


}
