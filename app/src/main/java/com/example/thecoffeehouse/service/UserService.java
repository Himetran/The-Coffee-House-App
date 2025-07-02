package com.example.thecoffeehouse.service;

import com.example.thecoffeehouse.model.User;
import com.example.thecoffeehouse.service.request.LoginRequest;
import com.example.thecoffeehouse.service.request.RegisterRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserService {
    @POST("users/register")
    Call<User> register(@Body RegisterRequest user);

    @POST("users/login")
    Call<User> login(@Body LoginRequest loginRequest);

    @GET("users/{id}")
    Call<User> getUserById(@Path("id") String id);
}

