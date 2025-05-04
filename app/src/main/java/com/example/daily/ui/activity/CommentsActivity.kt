package com.example.daily.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.daily.R
import com.example.daily.adapter.CommentsAdapter
import com.example.daily.ui.fragment.WebViewFragment
import com.example.daily.viewModel.LongViewModel
import com.example.daily.viewModel.ShortViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class CommentsActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var backBtn :Button
    private lateinit var shortViewModel: ShortViewModel
    private lateinit var longViewModel: LongViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)
        backBtn = findViewById(R.id.back_button)
        val storyId = intent.getLongExtra("storyId", -1L)
        if (storyId == -1L) {
            Toast.makeText(this, "评论加载失败", Toast.LENGTH_SHORT).show()
            finish()
            return
        }


        // 初始化 ViewModel（与 Fragment 共享）
        shortViewModel = ViewModelProvider(this)[ShortViewModel::class.java]
        longViewModel = ViewModelProvider(this)[LongViewModel::class.java]

        // 启动时立即加载评论
        shortViewModel.loadShortComments(storyId)
        longViewModel.loadLongComments(storyId)


        viewPager = findViewById(R.id.viewPager2)
        tabLayout = findViewById(R.id.tab_layout)

        val adapter = CommentsAdapter(this, storyId)
        viewPager.adapter = adapter

        // 绑定 TabLayout 与 ViewPager2
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = if (position == 0) "短评论" else "长评论"
        }.attach()
        initClick()
    }

    private fun initClick() {
        backBtn.setOnClickListener {
            finish()
        }
    }
}