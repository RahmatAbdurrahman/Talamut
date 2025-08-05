package com.android.ecoscan.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.ecoscan.data.model.News
import com.android.ecoscan.data.repository.NewsRepository
import kotlinx.coroutines.launch

class NewsViewModel(private val repository : NewsRepository) : ViewModel() {
    private val _newsList = MutableLiveData<List<News>>()
    val newsList: LiveData<List<News>> get() = _newsList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val isError: LiveData<String?> = _error

    fun fetchNews() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getNews(
                    query = "(reforestation OR afforestation OR deforestation OR forest restoration OR tree planting OR forest conservation) AND (environment OR climate OR biodiversity OR ecosystem)",
//                    query ="limbah OR polusi OR sampah OR bencana",
                    apiKey = "93a7944435a341608ad2ee163de24a97"
                )
                Log.d("NewsViewModel", "News fetched successfully: ${response.articles?.size} articles")
                _newsList.value = response.articles
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }


}