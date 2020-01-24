package com.example.esmarket.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.esmarket.Cart;
import com.example.esmarket.Common.Common;
import com.example.esmarket.Model.Order;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Database extends SQLiteAssetHelper {


    private static final String DB_NAME = "ESMarketDB.db" ;
    private static final int DB_VER = 1 ;

    public Database(Context context) {
        super(context,DB_NAME,null,DB_VER);
    }


    public List<Order> getCarts(String userPhone){



        SQLiteDatabase db  = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect={"UserPhone","ProductName","ProductId","Quantity","Price","Amount","Image"};
        String sqlTable="OrderDetail";

        qb.setTables(sqlTable);

        Cursor c = qb.query(db,sqlSelect,"UserPhone=?",new String[]{userPhone},null,null,null);

        final List<Order>  result = new ArrayList<>();
        if(c.moveToFirst()){

            do{
                result.add(new Order(
                        c.getString(c.getColumnIndex("UserPhone")),
                        c.getString(c.getColumnIndex("ProductId")),
                        c.getString(c.getColumnIndex("ProductName")),
                        c.getString(c.getColumnIndex("Quantity")),
                        c.getString(c.getColumnIndex("Price")),
                        c.getString(c.getColumnIndex("Amount")),
                        c.getString(c.getColumnIndex("Image"))
                ));

            }while(c.moveToNext());

        }

        return result;



    }

    public boolean checkPoductExists(String productId,String userPhone){

        SQLiteDatabase db =getReadableDatabase();

        boolean flag=false ;
        Cursor cursor ;
        String  SQLQuery = String.format("SELECT * FROM OrderDetail WHERE UserPhone='%s' AND ProductId='%s' ",userPhone,productId);
        cursor= db.rawQuery(SQLQuery,null);
        if(cursor.getCount()>0){

            flag=true ;
        }else
            flag=false ;
        cursor.close();

        return flag;


    }

    public void addToCart(Order order){

        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT OR REPLACE INTO OrderDetail(UserPhone,ProductId,ProductName,Quantity,Price,Amount,Image) VALUES('%s','%s','%s','%s','%s','%s','%s');",
                order.getUserPhone(),
                order.getProductId(),
                order.getProductName(),
                order.getQuantity(),
                order.getPrice(),
                order.getAmount(),
                order.getImage());

        db.execSQL(query);

    }

    public void cleanCart(String userPhone){

        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail WHERE UserPhone='%s' ",userPhone);

        db.execSQL(query);

    }


    public int getCountCart(String userPhone) {

        int count=0 ;

        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("SELECT COUNT(*) FROM OrderDetail  Where UserPhone='%s'",userPhone);

        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()){


            do{
                count=cursor.getInt(0);

            }while(cursor.moveToNext());

        }

        return count;
    }


    public void updateCart(Order order) {

        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("UPDATE OrderDetail SET Quantity= '%s' WHERE UserPhone = '%s' AND ProductId='%s' ",order.getQuantity(),
                order.getUserPhone(),order.getProductId());
        db.execSQL(query);










    }


    public void increaseCart(String userPhone,String productId) {

        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("UPDATE OrderDetail SET Quantity=Quantity+1 WHERE UserPhone='%s' AND ProductId='%s' ",userPhone,
                productId);
        db.execSQL(query);


    }


    public void removeFromCart(String productId, String phone) {

        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail  WHERE UserPhone='%s' and ProductId='%s' ",phone,productId);

        db.execSQL(query);

    }
}
