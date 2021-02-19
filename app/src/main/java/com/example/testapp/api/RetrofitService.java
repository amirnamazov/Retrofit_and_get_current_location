package com.example.testapp.api;

import com.example.testapp.model.Post;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitService {

    @GET("/posts")
    Call<ArrayList<Post>> getPost();
}
