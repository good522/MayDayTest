package com.example.daily.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daily.repository.NetRepository
import kotlinx.coroutines.launch
import com.example.daily.bean.Comment

class ShortViewModel : ViewModel() {
    private val _shortComments = MutableLiveData<List<Comment>>()
    val shortComments: LiveData<List<Comment>> get() = _shortComments

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun loadShortComments(storyId: Long) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = NetRepository.apiService.getShortComments(storyId)
                Log.d("ShortViewModel", "shortResponse: $response")
                _shortComments.value = response.comments
            } catch (e: Exception) {
                Log.e("ShortViewModel", "Error loading short comments: ${e.message}")
                e.printStackTrace()
                _shortComments.value = emptyList() // 或者保留上次数据
            }finally {
                _isLoading.value = false
            }
        }
    }
}