package com.android.ecoscan.data.api

import com.android.ecoscan.data.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {
    @GET("everything")
    suspend fun getNews(
        @Query("q") query: String ,
        @Query("language") language: String = "en",
        @Query("sortBy") sortBy: String = "relevancy",
        @Query("apiKey") apiKey: String
    ): NewsResponse
}