package com.example.daily.viewModel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.daily.bean.Story

class RvViewModel : ViewModel() {
    private val mutableList = mutableListOf<Story>()

    //初始化 _rvList 的初始值，防止第一次观察 .value 时为 null，造成 UI 层空指针错误
    private val _rvList = MutableLiveData<List<Story>>(emptyList())
    //防止其他类修改 .value
    val rvList: LiveData<List<Story>> get() = _rvList

    fun getRvList(list: List<Story>, append: Boolean = true) {
        //防止重复添加
        if (!append) mutableList.clear()
        mutableList.addAll(list)
        // 防止外部篡改
        _rvList.value = mutableList.toList()
    }
}
