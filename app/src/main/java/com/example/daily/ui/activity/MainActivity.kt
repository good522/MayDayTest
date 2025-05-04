package com.example.daily.ui.activity


import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.view.View
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
        accountButton = findViewById(R.id.account_button)
        dataTextView = findViewById(R.id.data_textView)
        viewPager = findViewById(R.id.banner_viewPager)
        indicatorsContainer = findViewById(R.id.banner_linearLayout)
        bannerAdapter = BannerAdapter(this, bannerList)
        viewPager.adapter = bannerAdapter
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            //页面滚动时回调
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }
            //页面选中时回调
            override fun onPageSelected(position: Int) {
                updateIndicators(bannerList.size, position)
            }
            override fun onPageScrollStateChanged(state: Int) {
            }
        })
        //启动时进行网络请求
        mainViewModel.topStories.observe(this) {
            bannerList.clear()
            bannerList.addAll(it)
            bannerAdapter.notifyDataSetChanged()
            createIndicators(bannerList.size, viewPager.currentItem)
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
    }

    private fun updateIndicators(size: Int, currentPosition: Int) {
        //限制循环范围更安全
        val count = minOf(indicatorsContainer.childCount, size)
        for (i in 0 until count) {
            val indicator = indicatorsContainer.getChildAt(i)
            indicator?.setBackgroundResource(
                if (i == currentPosition) R.drawable.indicator_selected
                else R.drawable.indicator_unselected
            )
        }
    }


    private fun createIndicators(size: Int, selectedPosition: Int) {
        //清除所有指示器,避免重复
        indicatorsContainer.removeAllViews()
        //循环创建指示器圆点
        for (i in 0 until size) {
            //创建空白View作为指示器圆点
            val indicator =View(this)
            val layoutParams = LinearLayout.LayoutParams(
                4.dpToPx(),   // 控制宽度
                4.dpToPx()    // 控制高度
            ).apply {
                marginStart = 3.dpToPx()
                marginEnd = 3.dpToPx()
            }
            indicator.layoutParams = layoutParams
            indicator.setBackgroundResource(if (i == selectedPosition) R.drawable.indicator_selected else R.drawable.indicator_unselected)
            indicatorsContainer.addView(indicator)
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

    private fun Int.dpToPx(): Int {
        //获取屏幕密度（dp与px的转换系数）
        val scale = resources.displayMetrics.density
        return (this * scale + 0.5f).toInt()
    }
}