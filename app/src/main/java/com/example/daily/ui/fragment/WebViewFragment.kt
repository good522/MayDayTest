package com.example.daily.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.daily.R
import com.example.daily.bean.Story
import com.example.daily.ui.activity.CommentsActivity
import com.example.daily.ui.activity.MainActivity
import com.example.daily.ui.activity.StoryActivity
import com.example.daily.ui.activity.TransmitActivity
import com.example.daily.viewModel.StoryViewModel

public class WebViewFragment : Fragment() {
    private lateinit var webView: WebView
    private lateinit var backBtn:Button
    private lateinit var commentsBtn:Button
    private lateinit var transmitBtn:Button
    private lateinit var loveBtn:Button
    private lateinit var collectBtn:Button
    private lateinit var storyViewModel: StoryViewModel
    private var story: Story? = null

    companion object {

        //newInstance() 是工厂方法,用于创建带参数的 WebViewFragment 实例，封装了 Bundle 设置，方便传入 Story
        fun newInstance(story: Story): WebViewFragment {
            val fragment = WebViewFragment()
            fragment.arguments = Bundle().apply {
                putParcelable("story", story)
                Log.d("WebViewFragment", "story: $story")
            }
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_web_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backBtn = view.findViewById(R.id.back_button)
        commentsBtn = view.findViewById(R.id.comments_button)
        transmitBtn = view.findViewById(R.id.transmit_button)
        loveBtn = view.findViewById(R.id.love_button)
        collectBtn = view.findViewById(R.id.collect_button)
        webView = view.findViewById(R.id.web_view)

        //在 WebView 启动时配置缓存，以防止加载资源重新耗时
        webView.settings.cacheMode = WebSettings.LOAD_DEFAULT
        webView.settings.javaScriptEnabled = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        webView.webViewClient = WebViewClient()

        // 获取传入的 Story 并保存为成员变量
        story = arguments?.getParcelable("story")
        Log.d("WebViewFragment", "getStory: $story")

        story?.let {
            Log.d("WebViewFragment", "Loading URL: ${it.url}")
            webView.loadUrl(it.url)
        }

        webView.webViewClient = object : WebViewClient() {
//            override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
//                super.onPageStarted(view, url, favicon)
//                // 显示加载进度条
//                showLoadingIndicator()
//            }
//
//            override fun onPageFinished(view: WebView?, url: String?) {
//                super.onPageFinished(view, url)
//                // 隐藏加载进度条
//                hideLoadingIndicator()
//            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                // 防止跳转到外部浏览器
                view?.loadUrl(url ?: "")
                return true
            }
        }

        // 获取共享的 ViewModel
        storyViewModel = ViewModelProvider(requireActivity())[StoryViewModel::class.java]

        initClick()
    }

    private fun initClick() {
        backBtn.setOnClickListener {
            if (canGoBack()) {
                goBack()
            } else {
                activity ?.finish()
            }
        }
        commentsBtn.setOnClickListener {
            story ?.let {
                val intent = Intent(requireContext(), CommentsActivity::class.java)
                intent.putExtra("storyId", it.id)
                startActivity(intent)
            }
        }
        transmitBtn.setOnClickListener {
            story?.let {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_SUBJECT, "分享 : ${it.title}")
                    putExtra(Intent.EXTRA_TEXT, it.url)
                    type = "text/plain"
                }
                startActivity(Intent.createChooser(intent, "分享到..."))
            }
        }
        loveBtn.setOnClickListener {
            story?.let {
                storyViewModel.toggleLove(it.url)  // 使用 story?.url
                val loved = storyViewModel.isLoved(it.url)
                loveBtn.setBackgroundResource(
                    if (loved) R.drawable.love_filled else R.drawable.love
                )
                Toast.makeText(
                    requireContext(),
                    if (loved) "已点赞" else "已取消点赞",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        collectBtn.setOnClickListener {
            story?.let {
                storyViewModel.toggleCollect(it.url)  // 使用 story?.url
                val collected = storyViewModel.isCollected(it.url)
                collectBtn.setBackgroundResource(
                    if (collected) R.drawable.collect_filled else R.drawable.collect
                )
                Toast.makeText(
                    requireContext(),
                    if (collected) "收藏成功" else "已取消收藏",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }
//    // 显示进度条
//    fun showLoadingIndicator() {
//        // 假设你有一个 ProgressBar 或其他加载指示器
//        progressBar.visibility = View.VISIBLE
//    }
//
//    // 隐藏进度条
//    fun hideLoadingIndicator() {
//        // 假设你有一个 ProgressBar 或其他加载指示器
//        progressBar.visibility = View.GONE
//    }

    //检查WebView是否可以回退到上一个页面
    fun canGoBack(): Boolean = this::webView.isInitialized && webView.canGoBack()
    //使WebView回退到上一个页面
    fun goBack() = webView.goBack()
}
