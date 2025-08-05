package com.android.ecoscan.data.model

data class NewsResponse (
    val status: String,
    val totalResults: Int,
    val articles: List<News>
)