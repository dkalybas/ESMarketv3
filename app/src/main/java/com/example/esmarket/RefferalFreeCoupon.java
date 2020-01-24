package com.example.esmarket;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.esmarket.Common.Common;
import com.example.esmarket.Model.Refferals;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RefferalFreeCoupon extends AppCompatActivity {


    FirebaseDatabase database;
    DatabaseReference refferals;

    Refferals refferal;

     int counterOfRef;
    //private static final int SHORT_DELAY = 2000; // 2 seconds

    private static final int RESULT_PICK_CONTACT =1;
    private TextView phone ;
    private Button select;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refferal_free_coupon);

        //FireBase

        database=FirebaseDatabase.getInstance();
        refferals = database.getReference("Refferals");


        phone=findViewById(R.id.phone);
        select=findViewById(R.id.select);



            select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if(Common.currentUser.isCoupon()&&counterOfRef<=3) {


                    Intent in = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                    startActivityForResult(in, RESULT_PICK_CONTACT);

                    }else {

                        Toast.makeText(RefferalFreeCoupon.this, "You already took the referral discount !!! ", Toast.LENGTH_SHORT).show();



                    }

                }
            });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){

            switch (requestCode){
                case RESULT_PICK_CONTACT:
                    contactPicked(data);
                    break;

            }

        }else{

            Toast.makeText(this,"Failed to pick a Contact ",Toast.LENGTH_LONG).show();

        }
    }


    private void contactPicked(Intent data){

        Cursor cursor = null;

        try {

            String phoneNo = null;
            Uri uri=data.getData();
            cursor=getContentResolver().query(uri,null,null,null,null);
            cursor.moveToFirst();
            int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            phoneNo=cursor.getString(phoneIndex);

            phone.setText(phoneNo);


            editText=   findViewById(R.id.editText);

            editText.setText(phoneNo);


            refferal=new Refferals(Common.currentUser.getPhone(),editText.getText().toString());

            refferals.child(String.valueOf(System.currentTimeMillis())).setValue(refferal);
            counterOfRef++;

            if(counterOfRef==3){

                Toast.makeText(this,"Excellent, thanks for the referral!! On your the next Transaction " +
                        "you will be granted 20 percent discount ",Toast.LENGTH_LONG).show();
                Common.currentUser.setCoupon(false);

            }

        }catch (Exception e){


            e.printStackTrace();

        }




    }

}
