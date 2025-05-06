package com.example.daily.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.daily.adapter.BannerAdapter
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.daily.R
import com.example.daily.adapter.ListAdapter
import com.example.daily.bean.Story
import com.example.daily.bean.TopStory
import com.example.daily.viewModel.MainViewModel
import com.example.daily.viewModel.RvViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var accountButton: Button
    private lateinit var dataTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewPager: ViewPager
    private lateinit var bannerAdapter: BannerAdapter
    private lateinit var listAdapter: ListAdapter
    private lateinit var rvViewModel: RvViewModel
    private lateinit var indicatorsContainer: LinearLayout
    private lateinit var mainViewModel: MainViewModel
    private var storyList: List<Story> = emptyList()
    private val bannerList = mutableListOf<TopStory>()
    private var isLoadingMore = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainViewModel  = ViewModelProvider(this)[MainViewModel::class.java]
        recyclerView = findViewById(R.id.rv_recyclerView)
        rvViewModel = ViewModelProvider(this)[RvViewModel::class.java]

        // 初始化 Adapter 并传入点击事件
        listAdapter = ListAdapter { position ->
            // 点击事件处理
            val intent = Intent(this, StoryActivity::class.java).apply {
                putParcelableArrayListExtra("storyList", ArrayList(storyList))
                putExtra("currentIndex", position)
            }
            startActivity(intent)
            Log.d("ListAdapter", "点击的 story: $position")
        }
        // 观察 ViewModel 中的数据
        mainViewModel.stories.observe(this) { list ->
            storyList = list//用于保存跳转参数
            listAdapter.submitList(list)
        }
        recyclerView.adapter = listAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount

                Log.d("Scroll", "Scrolled: lastVisible=$lastVisibleItem total=$totalItemCount")

                // 快到底部时加载更多
                if (lastVisibleItem >= totalItemCount - 2 && !isLoadingMore) {
                    Log.d("LoadMore", "check: last=$lastVisibleItem >= ${totalItemCount - 2}, isLoadingMore=$isLoadingMore")
                    Log.d("LoadMore", "条件成立？ lastVisible=$lastVisibleItem >= ${totalItemCount - 2}, isLoadingMore=$isLoadingMore")
                    isLoadingMore = true
                    val currentDate = mainViewModel.date.value ?: return
                    mainViewModel.loadMore(currentDate) {
                        isLoadingMore = false
                        listAdapter.notifyDataSetChanged()
                    }
                }
            }
        })
        accountButton = findViewById(R.id.account_button)
        dataTextView = findViewById(R.id.data_textView)
        //启动时进行网络请求

        mainViewModel.topStories.observe(this) {
            listAdapter.setBannerList(it)
        }

        mainViewModel.date.observe(this) {
            dataTextView.text = it
        }

        mainViewModel.fetchData() // 启动加载
        initClick()
    }

    private fun initClick() {
        accountButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        dataTextView.setOnClickListener {
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        listAdapter.stopAutoScroll()  // 通知 ViewHolder 停止轮播，避免内存泄露
    }
}