package com.example.thecoffeehouse.service;

import com.example.thecoffeehouse.model.Address;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AddressService {
    @GET("address/user/{userId}")
    Call<List<Address>> getAddressByUserId(@Path("userId") String userId);

    @POST("address")
    Call<Address> createAddress(@Body Address address);

    @PUT("address/{id}")
    Call<Address> updateAddress(@Path("id") String id, @Body Address address);

    @DELETE("address/{id}")
    Call<Void> deleteAddress(@Path("id") String id);
}
