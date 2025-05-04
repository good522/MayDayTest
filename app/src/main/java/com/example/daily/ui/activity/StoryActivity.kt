package com.example.daily.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.daily.R
import com.example.daily.adapter.ViewPagerAdapter
import com.example.daily.bean.Story
import com.example.daily.viewModel.StoryViewModel

class StoryActivity : AppCompatActivity() {
    private lateinit var storyViewModel: StoryViewModel
    private lateinit var viewPager: ViewPager2
    private lateinit var vpAdapter: ViewPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story)
        val storyList = intent.getParcelableArrayListExtra<Story>("storyList")?: arrayListOf()
        val currentIndex = intent.getIntExtra("currentIndex", 0)

        //初始化ViewModel并设置数据
        storyViewModel = ViewModelProvider(this)[StoryViewModel::class.java]
        storyViewModel.setStory(storyList)

        // 设置 ViewPager
        viewPager = findViewById(R.id.viewPager)
        vpAdapter = ViewPagerAdapter(this, storyViewModel)
        viewPager.adapter = vpAdapter
        viewPager.setCurrentItem(currentIndex, false)
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (position == 0){
                    Toast.makeText(this@StoryActivity, "已到达第一篇", Toast.LENGTH_SHORT).show()
                }else if (position == storyViewModel.getUrlCount() - 1){
                    Toast.makeText(this@StoryActivity, "已到达最后一篇", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}