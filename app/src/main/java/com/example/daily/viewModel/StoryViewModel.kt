package com.example.daily.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.daily.bean.Story

class StoryViewModel : ViewModel() {
    private val _storyList = MutableLiveData<List<Story>>()
    val storyList: LiveData<List<Story>> get() = _storyList

    // 添加likeMap和collectMap
    private val likeMap = mutableMapOf<String, Boolean>()
    private val collectMap = mutableMapOf<String, Boolean>()


    fun setStory(stories: List<Story>) {
        _storyList.value = stories
    }

    fun getStoryByPosition(position: Int): Story? {
        //一个安全调用方法，用于避免索引越界异常
        return _storyList.value?.getOrNull(position)
    }

    fun getUrlCount(): Int = _storyList.value?.size ?: 0
    fun isLoved(url: String): Boolean = likeMap[url] ?: false
    fun toggleLove(url: String) {
        likeMap[url] = !(likeMap[url] ?: false)
    }

    // 收藏功能
    fun isCollected(url: String): Boolean = collectMap[url] ?: false
    fun toggleCollect(url: String) {
        collectMap[url] = !(collectMap[url] ?: false)
    }
}