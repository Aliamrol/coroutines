package com.example.coroutines.data.remote

import com.example.coroutines.data.model.Post
import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {

    @GET("/posts")
    suspend fun getAllPosts(): Response<List<Post>>
}