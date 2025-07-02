package com.example.thecoffeehouse.service;

import com.example.thecoffeehouse.model.CartItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CartService {
    @GET("cart/user/{userId}")
    Call<List<CartItem>> getCartByUserId(@Path("userId") String userId);

    @POST("cart")
    Call<CartItem> addToCart(@Body CartItem item);

    @PUT("cart/{id}")
    Call<CartItem> updateCartItem(@Path("id") String id, @Body CartItem item);

    @DELETE("cart/{id}")
    Call<Void> deleteCartItem(@Path("id") String id);
}

