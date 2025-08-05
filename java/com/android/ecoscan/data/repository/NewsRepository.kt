package com.android.ecoscan.data.repository

import com.android.ecoscan.data.api.ApiService
import com.android.ecoscan.data.model.NewsResponse

class NewsRepository(private val apiService: ApiService) {
    suspend fun getNews(
        query: String,
        apiKey: String
    ): NewsResponse {
        return apiService.getNews(
            query = query,
            apiKey = apiKey
        )
    }
}
