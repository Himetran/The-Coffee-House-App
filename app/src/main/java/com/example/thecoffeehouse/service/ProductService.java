package com.example.thecoffeehouse.service;

import com.example.thecoffeehouse.model.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ProductService {
    @GET("products")
    Call<List<Product>> getAllProducts();

    @GET("products/{id}")
    Call<Product> getProductById(@Path("id") String id);
}
