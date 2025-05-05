package com.example.daily.adapter


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.daily.R
import com.example.daily.bean.Story
import com.example.daily.bean.TopStory

class ListAdapter (private val onItemClick: (Long) -> Unit
): androidx.recyclerview.widget.ListAdapter<Story, RecyclerView.ViewHolder>(
    //用于高效更新数据，帮助实现自动刷新
    object : DiffUtil.ItemCallback<Story>() {
        //比较两个item是否为同一个
        override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
            return oldItem.id == newItem.id
        }
        //比较两个item的内容是否相同
        override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
            return oldItem == newItem
        }
    }
) {
    //  item的布局
    inner class LtViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title_tv: TextView = itemView.findViewById(R.id.title_textView)
        val hint_tv: TextView = itemView.findViewById(R.id.hint_textView)
        val rv_img: ImageView = itemView.findViewById(R.id.rv_imageView)
    }
    inner class BannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val viewPager = itemView.findViewById<ViewPager>(R.id.banner_viewPager)
        val indicatorsContainer = itemView.findViewById<LinearLayout>(R.id.banner_linearLayout)
        fun bind(banners: List<TopStory>) {
            val adapter = BannerAdapter(itemView.context, banners)
            if (viewPager != null) {
                viewPager.adapter = adapter
            } else {
                Log.e("ListAdapter", "viewPager 为空")
            }

            createIndicators(banners.size, viewPager.currentItem)

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

                override fun onPageScrollStateChanged(state: Int) {}
            })
        }

        private fun createIndicators(size: Int, selectedPosition: Int) {
            //清除所有指示器,避免重复
            indicatorsContainer.removeAllViews()
            //循环创建指示器圆点
            for (i in 0 until size) {
                //创建空白View作为指示器圆点
                val indicator = View(itemView.context)
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

        private fun Int.dpToPx(): Int {
            //获取屏幕密度（dp与px的转换系数）
            val scale = itemView.context.resources.displayMetrics.density
            return (this * scale + 0.5f).toInt()
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
    }
    private var bannerList: List<TopStory> = emptyList()
    fun setBannerList(banners: List<TopStory>) {
        bannerList = banners
        notifyItemChanged(0)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_BANNER){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_banner_item, parent, false)
            BannerViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_item, parent, false)
            LtViewHolder(view)
        }
    }

    // 绑定数据
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is BannerViewHolder){
            holder.bind(bannerList)
        }else if ( holder is LtViewHolder){
            // 获取数据
            val rvData = getItem(position -1)
            holder.title_tv.text = rvData.title
            holder.hint_tv.text = rvData.hint

            Glide.with(holder.rv_img.context)
                .load(rvData.images.firstOrNull())
                .placeholder(R.drawable.placeholder) // 设置加载中的占位图
                .error(R.drawable.error_image) // 设置加载失败的错误图
                .into(holder.rv_img)

            holder.itemView.setOnClickListener {
                onItemClick(rvData.id) // 传递 id
            }
        }
    }
    companion object {
        private const val VIEW_TYPE_BANNER = 0
        private const val VIEW_TYPE_STORY = 1
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + 1 // 多一个 banner
    }
    override fun getItemViewType(position: Int): Int {
        return if (position == 0) VIEW_TYPE_BANNER else VIEW_TYPE_STORY
    }

}
