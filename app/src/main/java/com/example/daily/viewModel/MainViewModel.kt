package com.example.daily.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daily.bean.Story
import com.example.daily.bean.TopStory
import com.example.daily.repository.NetRepository
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _topStories = MutableLiveData<List<TopStory>>()
    val topStories: LiveData<List<TopStory>> get() = _topStories

    private val _stories = MutableLiveData<List<Story>>()
    val stories: LiveData<List<Story>> get() = _stories

    private val _date = MutableLiveData<String>()
    val date: LiveData<String> get() = _date

    fun fetchData() {
        viewModelScope.launch {
            try {
                val latest = NetRepository.apiService.getLatest()
                _topStories.value = latest.top_stories
                _stories.value = latest.stories
                _date.value = latest.date
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun loadMore(date: String) {
        viewModelScope.launch {
            try {
                val before = NetRepository.apiService.getBefore(date)
                val newList = (_stories.value ?: emptyList()) + (before.stories ?: emptyList())
                _stories.value = newList
                // 更新日期为加载到的旧日期
                _date.value = before.date
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}


