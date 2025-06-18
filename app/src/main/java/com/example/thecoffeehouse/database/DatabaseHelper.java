package com.example.thecoffeehouse.database; // 👉 nhớ đổi thành đúng package bạn dùng

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "TheCoffeHouse.db";
    public static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE categories (" +
                "category_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "icon_url TEXT," +
                "display_order INTEGER," +
                "is_visible INTEGER DEFAULT 1," +
                "description TEXT)");

        db.execSQL("CREATE TABLE address (" +
                "id INTEGER," +
                "user_id INTEGER NOT NULL," +
                "description TEXT NOT NULL)");

        db.execSQL("CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "phone TEXT UNIQUE," +
                "password TEXT," +
                "name TEXT," +
                "date_of_birth TEXT," +
                "gender INTEGER)");

        db.execSQL("CREATE TABLE orders (" +
                "order_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "order_date TEXT NOT NULL," +
                "customer_name TEXT," +
                "customer_phone TEXT," +
                "delivery_address TEXT NOT NULL," +
                "delivery_note TEXT," +
                "store_id INTEGER," +
                "total_amount REAL NOT NULL," +
                "status TEXT NOT NULL," +
                "user_id INTEGER NOT NULL)");

        db.execSQL("CREATE TABLE order_details (" +
                "order_detail_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "order_id INTEGER NOT NULL," +
                "product_id INTEGER NOT NULL," +
                "quantity INTEGER NOT NULL," +
                "unit_price REAL NOT NULL," +
                "subtotal REAL NOT NULL," +
                "sugar_level TEXT," +
                "ice_level TEXT," +
                "note TEXT)");

        db.execSQL("CREATE TABLE store (" +
                "store_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "address INTEGER)");

        db.execSQL("CREATE TABLE products (" +
                "product_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "description TEXT," +
                "price REAL NOT NULL," +
                "sale_price REAL," +
                "image_url TEXT," +
                "category_id INTEGER," +
                "is_bestseller INTEGER DEFAULT 0," +
                "is_recommended INTEGER DEFAULT 0," +
                "is_available INTEGER DEFAULT 1)");

        db.execSQL("CREATE TABLE toppings (" +
                "topping_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "price REAL)");

        db.execSQL("CREATE TABLE orders_toppings (" +
                "order_topping_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "order_detail_id INTEGER NOT NULL," +
                "topping_id INTEGER NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS orders_toppings");
        db.execSQL("DROP TABLE IF EXISTS toppings");
        db.execSQL("DROP TABLE IF EXISTS products");
        db.execSQL("DROP TABLE IF EXISTS store");
        db.execSQL("DROP TABLE IF EXISTS order_details");
        db.execSQL("DROP TABLE IF EXISTS orders");
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS address");
        db.execSQL("DROP TABLE IF EXISTS categories");
        onCreate(db);
    }

    public Cursor checkUsernamePassword(String phone, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("Select * from users where phone = ? and password = ?", new String[]{phone, password});
    }
}
