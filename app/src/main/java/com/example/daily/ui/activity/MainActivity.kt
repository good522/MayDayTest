package com.example.daily.ui.activity


import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.daily.adapter.BannerAdapter
import android.os.Handler
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
        rvViewModel.rvList.observe(this) { list ->
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

                // 快到底部时加载更多，避免重复触发可添加一个加载标志位
                if (lastVisibleItem >= totalItemCount - 5 && !isLoadingMore) {
                    isLoadingMore = true
                    val currentDate = mainViewModel.date.value ?: return
                    mainViewModel.loadMore(currentDate)
                }
            }
        })
        accountButton = findViewById(R.id.account_button)
        dataTextView = findViewById(R.id.data_textView)
        //启动时进行网络请求

        mainViewModel.topStories.observe(this) {
            listAdapter.setBannerList(it)
        }

        mainViewModel.stories.observe(this) { list ->
            storyList = list
            listAdapter.submitList(list)
        }

        mainViewModel.date.observe(this) {
            dataTextView.text = it
        }

        mainViewModel.fetchData() // 启动加载
        initClick()
        startAutoScroll()
    }

    private fun initClick() {
        accountButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        dataTextView.setOnClickListener {
        }
    }

    private var autoScrollHandler: Handler? = null

    private val autoScrollRunnable = object : Runnable {
        override fun run() {
            if (bannerList.isNotEmpty()) {
                val currentItem = viewPager.currentItem
                val nextItem = (currentItem + 1) % bannerList.size
                viewPager.setCurrentItem(nextItem, true)

                // 继续下一次滚动
                autoScrollHandler?.postDelayed(this, 5000)
            }
        }
    }

    private fun startAutoScroll() {
        if (autoScrollHandler == null) {
            autoScrollHandler = Handler(Looper.getMainLooper())
        }
        autoScrollHandler?.postDelayed(autoScrollRunnable, 5000)
    }

    override fun onDestroy() {
        super.onDestroy()
        // 停止自动滚动，防止内存泄露
        autoScrollHandler?.removeCallbacks(autoScrollRunnable)
        autoScrollHandler = null
    }
}