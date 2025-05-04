package com.example.daily.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daily.repository.NetRepository
import kotlinx.coroutines.launch
import com.example.daily.bean.Comment

class LongViewModel : ViewModel() {

    private val _longComments = MutableLiveData<List<Comment>>()
    val longComments: LiveData<List<Comment>> get() = _longComments

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun loadLongComments(storyId: Long) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = NetRepository.apiService.getLongComments(storyId)
                Log.d("LongViewModel", "loadLongComments: ${response.comments}")
                _longComments.value = response.comments
            } catch (e: Exception) {
                e.printStackTrace()
                _longComments.value = emptyList() // 或者保留上次数据
            } finally {
                _isLoading.value = false
            }
        }
    }
}