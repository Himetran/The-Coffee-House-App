package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Info
    private static final String DATABASE_NAME = "TheCoffeeHouseDB.db";
    private static final int DATABASE_VERSION = 3;

    // Table Names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_CATEGORIES = "categories";
    private static final String TABLE_PRODUCTS = "products";
    private static final String TABLE_ORDERS = "orders";
    private static final String TABLE_ORDER_DETAILS = "order_details";
    private static final String TABLE_ORDER_TOPPINGS = "order_toppings";

    // User Table Columns
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_PHONE = "phone";

    // Category Table Columns
    private static final String COLUMN_CATEGORY_ID = "category_id";
    private static final String COLUMN_CATEGORY_NAME = "name";
    private static final String COLUMN_CATEGORY_ICON = "icon_url";
    private static final String COLUMN_DISPLAY_ORDER = "display_order";
    private static final String COLUMN_IS_VISIBLE = "is_visible";
    private static final String COLUMN_CATEGORY_DESCRIPTION = "description";

    // Product Table Columns
    private static final String COLUMN_PRODUCT_ID = "product_id";
    private static final String COLUMN_PRODUCT_NAME = "name";
    private static final String COLUMN_PRODUCT_DESC = "description";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_SALE_PRICE = "sale_price";
    private static final String COLUMN_IMAGE_URL = "image_url";
    private static final String COLUMN_FK_CATEGORY_ID = "category_id";
    private static final String COLUMN_IS_BESTSELLER = "is_bestseller";
    private static final String COLUMN_IS_RECOMMENDED = "is_recommended";
    private static final String COLUMN_IS_AVAILABLE = "is_available";

    // Order Table Columns
    private static final String COLUMN_ORDER_ID = "order_id";
    private static final String COLUMN_ORDER_DATE = "order_date";
    private static final String COLUMN_CUSTOMER_NAME = "customer_name";
    private static final String COLUMN_CUSTOMER_PHONE = "customer_phone";
    private static final String COLUMN_DELIVERY_ADDRESS = "delivery_address";
    private static final String COLUMN_DELIVERY_NOTE = "delivery_note";
    private static final String COLUMN_STORE_ID = "store_id";
    private static final String COLUMN_TOTAL_AMOUNT = "total_amount";
    private static final String COLUMN_STATUS = "status";

    // Order Detail Table Columns
    private static final String COLUMN_ORDER_DETAIL_ID = "order_detail_id";
    private static final String COLUMN_FK_ORDER_ID = "order_id";
    private static final String COLUMN_FK_PRODUCT_ID = "product_id";
    private static final String COLUMN_QUANTITY = "quantity";
    private static final String COLUMN_UNIT_PRICE = "unit_price";
    private static final String COLUMN_SUBTOTAL = "subtotal";
    private static final String COLUMN_SUGAR_LEVEL = "sugar_level";
    private static final String COLUMN_ICE_LEVEL = "ice_level";
    private static final String COLUMN_NOTE = "note";

    // Order Topping Table Columns
    private static final String COLUMN_ORDER_TOPPING_ID = "order_topping_id";
    private static final String COLUMN_FK_ORDER_DETAIL_ID = "order_detail_id";
    private static final String COLUMN_TOPPING_NAME = "topping_name";
    private static final String COLUMN_TOPPING_PRICE = "topping_price";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng users
        String createTableUsers = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PHONE + " TEXT UNIQUE)";
        db.execSQL(createTableUsers);

        // Tạo bảng categories
        String createTableCategories = "CREATE TABLE " + TABLE_CATEGORIES + "(" +
                COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CATEGORY_NAME + " TEXT NOT NULL, " +
                COLUMN_CATEGORY_ICON + " TEXT, " +
                COLUMN_DISPLAY_ORDER + " INTEGER, " +
                COLUMN_IS_VISIBLE + " INTEGER DEFAULT 1, " +
                COLUMN_CATEGORY_DESCRIPTION + " TEXT" +
                ")";
        db.execSQL(createTableCategories);

        // Tạo bảng products
        String createTableProducts = "CREATE TABLE " + TABLE_PRODUCTS + "(" +
                COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PRODUCT_NAME + " TEXT NOT NULL, " +
                COLUMN_PRODUCT_DESC + " TEXT, " +
                COLUMN_PRICE + " REAL NOT NULL, " +
                COLUMN_SALE_PRICE + " REAL, " +
                COLUMN_IMAGE_URL + " TEXT, " +
                COLUMN_FK_CATEGORY_ID + " INTEGER, " +
                COLUMN_IS_BESTSELLER + " INTEGER DEFAULT 0, " +
                COLUMN_IS_RECOMMENDED + " INTEGER DEFAULT 0, " +
                COLUMN_IS_AVAILABLE + " INTEGER DEFAULT 1, " +
                "FOREIGN KEY (" + COLUMN_FK_CATEGORY_ID + ") REFERENCES " +
                TABLE_CATEGORIES + "(" + COLUMN_CATEGORY_ID + ")" +
                ")";
        db.execSQL(createTableProducts);

        // Tạo bảng orders
        String createTableOrders = "CREATE TABLE " + TABLE_ORDERS + "(" +
                COLUMN_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ORDER_DATE + " TEXT NOT NULL, " +
                COLUMN_CUSTOMER_NAME + " TEXT, " +
                COLUMN_CUSTOMER_PHONE + " TEXT, " +
                COLUMN_DELIVERY_ADDRESS + " TEXT NOT NULL, " +
                COLUMN_DELIVERY_NOTE + " TEXT, " +
                COLUMN_STORE_ID + " INTEGER, " +
                COLUMN_TOTAL_AMOUNT + " REAL NOT NULL, " +
                COLUMN_STATUS + " TEXT NOT NULL" +
                ")";
        db.execSQL(createTableOrders);

        // Tạo bảng order_details
        String createTableOrderDetails = "CREATE TABLE " + TABLE_ORDER_DETAILS + "(" +
                COLUMN_ORDER_DETAIL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FK_ORDER_ID + " INTEGER NOT NULL, " +
                COLUMN_FK_PRODUCT_ID + " INTEGER NOT NULL, " +
                COLUMN_QUANTITY + " INTEGER NOT NULL, " +
                COLUMN_UNIT_PRICE + " REAL NOT NULL, " +
                COLUMN_SUBTOTAL + " REAL NOT NULL, " +
                COLUMN_SUGAR_LEVEL + " TEXT, " +
                COLUMN_ICE_LEVEL + " TEXT, " +
                COLUMN_NOTE + " TEXT, " +
                "FOREIGN KEY (" + COLUMN_FK_ORDER_ID + ") REFERENCES " +
                TABLE_ORDERS + "(" + COLUMN_ORDER_ID + "), " +
                "FOREIGN KEY (" + COLUMN_FK_PRODUCT_ID + ") REFERENCES " +
                TABLE_PRODUCTS + "(" + COLUMN_PRODUCT_ID + ")" +
                ")";
        db.execSQL(createTableOrderDetails);

        // Tạo bảng order_toppings
        String createTableOrderToppings = "CREATE TABLE " + TABLE_ORDER_TOPPINGS + "(" +
                COLUMN_ORDER_TOPPING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FK_ORDER_DETAIL_ID + " INTEGER NOT NULL, " +
                COLUMN_TOPPING_NAME + " TEXT NOT NULL, " +
                COLUMN_TOPPING_PRICE + " REAL NOT NULL, " +
                "FOREIGN KEY (" + COLUMN_FK_ORDER_DETAIL_ID + ") REFERENCES " +
                TABLE_ORDER_DETAILS + "(" + COLUMN_ORDER_DETAIL_ID + ")" +
                ")";
        db.execSQL(createTableOrderToppings);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Nếu từ version 1 lên version 2: thêm bảng categories và products
            // Không xóa bảng users để giữ lại dữ liệu người dùng
            // Tạo bảng categories
            String createTableCategories = "CREATE TABLE IF NOT EXISTS " + TABLE_CATEGORIES + "(" +
                    COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_CATEGORY_NAME + " TEXT NOT NULL, " +
                    COLUMN_CATEGORY_ICON + " TEXT, " +
                    COLUMN_DISPLAY_ORDER + " INTEGER, " +
                    COLUMN_IS_VISIBLE + " INTEGER DEFAULT 1, " +
                    COLUMN_CATEGORY_DESCRIPTION + " TEXT" +
                    ")";
            db.execSQL(createTableCategories);

            // Tạo bảng products
            String createTableProducts = "CREATE TABLE IF NOT EXISTS " + TABLE_PRODUCTS + "(" +
                    COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_PRODUCT_NAME + " TEXT NOT NULL, " +
                    COLUMN_PRODUCT_DESC + " TEXT, " +
                    COLUMN_PRICE + " REAL NOT NULL, " +
                    COLUMN_SALE_PRICE + " REAL, " +
                    COLUMN_IMAGE_URL + " TEXT, " +
                    COLUMN_FK_CATEGORY_ID + " INTEGER, " +
                    COLUMN_IS_BESTSELLER + " INTEGER DEFAULT 0, " +
                    COLUMN_IS_RECOMMENDED + " INTEGER DEFAULT 0, " +
                    COLUMN_IS_AVAILABLE + " INTEGER DEFAULT 1, " +
                    "FOREIGN KEY (" + COLUMN_FK_CATEGORY_ID + ") REFERENCES " +
                    TABLE_CATEGORIES + "(" + COLUMN_CATEGORY_ID + ")" +
                    ")";
            db.execSQL(createTableProducts);
        }

        if (oldVersion < 3) {
            // Nếu từ version 2 lên version 3: thêm bảng orders, order_details và order_toppings
            // Tạo bảng orders
            String createTableOrders = "CREATE TABLE IF NOT EXISTS " + TABLE_ORDERS + "(" +
                    COLUMN_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_ORDER_DATE + " TEXT NOT NULL, " +
                    COLUMN_CUSTOMER_NAME + " TEXT, " +
                    COLUMN_CUSTOMER_PHONE + " TEXT, " +
                    COLUMN_DELIVERY_ADDRESS + " TEXT NOT NULL, " +
                    COLUMN_DELIVERY_NOTE + " TEXT, " +
                    COLUMN_STORE_ID + " INTEGER, " +
                    COLUMN_TOTAL_AMOUNT + " REAL NOT NULL, " +
                    COLUMN_STATUS + " TEXT NOT NULL" +
                    ")";
            db.execSQL(createTableOrders);

            // Tạo bảng order_details
            String createTableOrderDetails = "CREATE TABLE IF NOT EXISTS " + TABLE_ORDER_DETAILS + "(" +
                    COLUMN_ORDER_DETAIL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_FK_ORDER_ID + " INTEGER NOT NULL, " +
                    COLUMN_FK_PRODUCT_ID + " INTEGER NOT NULL, " +
                    COLUMN_QUANTITY + " INTEGER NOT NULL, " +
                    COLUMN_UNIT_PRICE + " REAL NOT NULL, " +
                    COLUMN_SUBTOTAL + " REAL NOT NULL, " +
                    COLUMN_SUGAR_LEVEL + " TEXT, " +
                    COLUMN_ICE_LEVEL + " TEXT, " +
                    COLUMN_NOTE + " TEXT, " +
                    "FOREIGN KEY (" + COLUMN_FK_ORDER_ID + ") REFERENCES " +
                    TABLE_ORDERS + "(" + COLUMN_ORDER_ID + "), " +
                    "FOREIGN KEY (" + COLUMN_FK_PRODUCT_ID + ") REFERENCES " +
                    TABLE_PRODUCTS + "(" + COLUMN_PRODUCT_ID + ")" +
                    ")";
            db.execSQL(createTableOrderDetails);

            // Tạo bảng order_toppings
            String createTableOrderToppings = "CREATE TABLE IF NOT EXISTS " + TABLE_ORDER_TOPPINGS + "(" +
                    COLUMN_ORDER_TOPPING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_FK_ORDER_DETAIL_ID + " INTEGER NOT NULL, " +
                    COLUMN_TOPPING_NAME + " TEXT NOT NULL, " +
                    COLUMN_TOPPING_PRICE + " REAL NOT NULL, " +
                    "FOREIGN KEY (" + COLUMN_FK_ORDER_DETAIL_ID + ") REFERENCES " +
                    TABLE_ORDER_DETAILS + "(" + COLUMN_ORDER_DETAIL_ID + ")" +
                    ")";
            db.execSQL(createTableOrderToppings);
        }
    }
}