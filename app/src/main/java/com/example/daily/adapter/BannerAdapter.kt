package com.example.daily.adapter


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.example.daily.R
import com.example.daily.bean.TopStory
import com.example.daily.ui.activity.StoryActivity

class BannerAdapter (
    private val context: Context,
    private val bannerList: List<TopStory>
) : PagerAdapter() {
    private val inflater = LayoutInflater.from(context)
    override fun getCount(): Int {
        return bannerList.size
        //返回轮播图数据总数
    }
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
        //判断当前View是否是ViewPager的子View
    }
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = inflater.inflate(R.layout.banner_item, container,  false)
        //container是ViewPager,false表示不立即添加到父容器中
        val title = view.findViewById<TextView>(R.id.banner_title)
        val hint = view.findViewById<TextView>(R.id.banner_hint)
        val imageView = view.findViewById<ImageView>(R.id.banner_image)
        Glide.with(context).load(bannerList[position].image)
            .placeholder(R.drawable.placeholder) // 设置加载中的占位图
            .error(R.drawable.error_image) // 设置加载失败的错误图
            .into(imageView)

        title.text = bannerList[position].title
        hint.text = bannerList[position].hint

        view.setOnClickListener {
            //获取当前点击的item数据
            val item = bannerList[position]
            //创建跳转意图

            val intent = Intent(context, StoryActivity::class.java).apply {
                Log.d("BannerAdapter", "item: $item")
                putExtra("id", item.id)
                if (context !is Activity) {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            }
            context.startActivity(intent)
        }
        container.addView(view)
        return view
    }
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
        //销毁ViewPager的子View
    }
}
