package com.example.testapp.api;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceBuilder {

    private final String BASE_URL = "http://jsonplaceholder.typicode.com/";

    private final OkHttpClient client = new OkHttpClient.Builder().build();

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public RetrofitService retrofitService = retrofit.create(RetrofitService.class);
}
