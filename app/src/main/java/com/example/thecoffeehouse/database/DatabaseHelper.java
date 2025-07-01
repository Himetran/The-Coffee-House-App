package com.example.thecoffeehouse.database; // 👉 nhớ đổi thành đúng package bạn dùng

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.thecoffeehouse.database.Table.CartTable;
import com.example.thecoffeehouse.database.Table.OrderDetailTable;
import com.example.thecoffeehouse.model.Address;
import com.example.thecoffeehouse.model.CartItem;
import com.example.thecoffeehouse.model.Order;
import com.example.thecoffeehouse.model.OrderDetail;
import com.example.thecoffeehouse.model.User;

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
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER NOT NULL," +
                "name TEXT NOT NULL," +
                "phone TEXT NOT NULL," +
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
                "status int NOT NULL," +
                "user_id INTEGER NOT NULL," +
                "order_code TEXT NOT NULL)");

        db.execSQL("CREATE TABLE order_details (" +
                "order_detail_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "order_code TEXT NOT NULL," +
                "product_id INTEGER NOT NULL," +
                "quantity INTEGER NOT NULL," +
                "topping_id INTEGER NOT NULL," +
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
        String CREATE_CART_TABLE = "CREATE TABLE " + CartTable.TB_NAME + "("
                + CartTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CartTable.COLUMN_USER_ID + " TEXT,"
                + CartTable.COLUMN_QUANTITY + " INTEGER,"
                + CartTable.COLUMN_PRODUCT_ID + " TEXT,"
                + CartTable.COLUMN_PRODUCT_NAME + " TEXT,"
                + CartTable.COLUMN_PRODUCT_PRICE + " REAL,"
                + CartTable.COLUMN_PRODUCT_IMAGE + " TEXT" + ")";
        db.execSQL(CREATE_CART_TABLE);
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
        db.execSQL("DROP TABLE IF EXISTS cart");
        onCreate(db);
    }

    public Cursor checkUsernamePassword(String phone, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("Select * from users where phone = ? and password = ?", new String[]{phone, password});
    }

    public Cursor findProduct(String productName) {
        SQLiteDatabase db = this.getWritableDatabase();

        if (productName == null || productName.trim().isEmpty()) {
            return db.rawQuery("SELECT * FROM products", null);
        } else {
            return db.rawQuery("SELECT * FROM products WHERE name LIKE ?", new String[]{"%" + productName + "%"});
        }
    }

    public Cursor getAllCartByUserIdAndProductId(int userId, int productId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select c.id, p.product_id, p.price as product_price, c.user_id, p.name as product_name, c.quantity, p.image_url as product_image from cart c inner join main.products p on p.product_id = c.product_id where c.user_id = ? and p.product_id = ?", new String[]{String.valueOf(userId), String.valueOf(productId)}, null);
    }

    public Cursor getCountCart(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select count(*) from cart c inner join main.products p on p.product_id = c.product_id where c.user_id = ?", new String[]{String.valueOf(userId)}, null);
    }

    public void insertCart(int userId, int quantity, int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("insert into cart(user_id, quantity, product_id) values (?,?,?)",
                new Object[]{userId, quantity, productId});
        db.close();
    }

    public void updateCart(int id, int productId, int quantity, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE cart SET quantity=? WHERE id=? and product_id=? and user_id=?",
                new Object[]{quantity, id, productId, userId});
        db.close();
    }

    public void deleteCartItem(CartItem cartItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM cart WHERE user_id=? and id = ?", new Object[]{cartItem.getUserId(), cartItem.getId()});
        db.close();
    }

    public void deleteCart(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM cart WHERE user_id=?", new Object[]{userId});
        db.close();
    }


    public Cursor getAllCartByUserId(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select c.id, p.product_id, p.price as product_price, c.user_id, p.name as product_name, c.quantity, p.image_url as product_image from cart c inner join main.products p on p.product_id = c.product_id where c.user_id = ?", new String[]{String.valueOf(userId)}, null);
    }

    public Cursor getUserById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select u.id, u.name, u.password, u.phone from users u where u.id = ?", new String[]{String.valueOf(userId)}, null);
    }

    public Cursor getUserByPhone(String phone) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select u.id, u.name, u.password, u.phone from users u where u.phone = ?", new String[]{String.valueOf(phone)}, null);
    }

    public Cursor getAddressByUserId(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select a.id, a.user_id, a.name, a.description as address, a.phone from address a where a.user_id = ?", new String[]{String.valueOf(userId)}, null);
    }

    public void insertOrder(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("insert into orders (order_date, customer_name, customer_phone, delivery_address, delivery_note, store_id,\n" +
                        "                    total_amount, status, user_id, order_code)\n" +
                        "values (?, ?, ?,?,?,?,?,?,?,?)",
                new Object[]{order.getOrderDate(), order.getCustomerName(), order.getCustomerPhone(), order.getDeliveryAddress(), order.getDeliveryNote(), order.getStoreId(), order.getTotalAmount(), order.getStatus(), order.getUserId(), order.getOrderCode()});
        db.close();
    }

    public void insertOrderDetail(OrderDetail orderDetail) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("insert into order_details (order_code, product_id, quantity, note, topping_id)\n" +
                        "values (?, ?, ?, ?,?)",
                new Object[]{orderDetail.getOrderCode(), orderDetail.getProductId(), orderDetail.getQuantity(), orderDetail.getNote(), orderDetail.getToppingId()});
        db.close();
    }

    public Cursor getOrderByUserId(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select * from orders where user_id = ?", new String[]{String.valueOf(userId)}, null);
    }

    public Cursor getOrderDetailByOrderCode(String orderCode) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select p.name, o.quantity, p.price, p.image_url\n" +
                "from order_details o inner join products p on o.product_id = p.product_id where order_code = ?", new String[]{String.valueOf(orderCode)}, null);
    }


    public void insertAddress(Address address) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("insert into address (user_id, description, phone , name)\n" +
                        "values (?, ?, ?, ?)",
                new Object[]{address.getUserId(), address.getAddress(), address.getPhone(), address.getName()});
        db.close();
    }

    public void updateAddress(Address address) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE address\n" +
                        "SET name = ?, description = ?, phone = ?\n" +
                        "WHERE user_id = ?",
                new Object[]{address.getName(), address.getAddress(), address.getPhone(), address.getUserId()});
        db.close();
    }

    public void deleteAddress(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete\n" +
                        "from address\n" +
                        "where id = ?",
                new Object[]{id});
        db.close();
    }

    public void insertUser(User user, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("insert into users (phone, password, name)\n" +
                        "values (?, ?, ?)",
                new Object[]{user.getPhone(), password, user.getName()});
        db.close();
    }
}
