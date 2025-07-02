package com.example.thecoffeehouse.service;

import com.example.thecoffeehouse.model.Order;
import com.example.thecoffeehouse.model.OrderDetail;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface OrderService {

    @POST("orders")
    Call<Order> createOrder(@Body Order order);

    @POST("orders/details")
    Call<OrderDetail> createOrderDetail(@Body OrderDetail detail);

    @GET("orders/user/{userId}")
    Call<List<Order>> getOrdersByUser(@Path("userId") String userId);

    @GET("orders/details/{orderCode}")
    Call<List<OrderDetail>> getOrderDetails(@Path("orderCode") String orderCode);
}
