//package com.example.daily.adapter
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.example.daily.R
//import com.example.daily.bean.Story
//
//class StoryAdapter (private val onItemClick: (storyId: Int) -> Unit
//): androidx.recyclerview.widget.ListAdapter<Story, StoryAdapter.StoryViewHolder>(
//    //用于高效更新数据，帮助实现自动刷新
//    object : DiffUtil.ItemCallback<Story>() {
//        //比较两个item是否为同一个
//        override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
//            return oldItem.id == newItem.id
//        }
//        //比较两个item的内容是否相同
//        override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
//            return oldItem == newItem
//        }
//    }
//) {
//    //  item的布局
//    inner class StoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val title_tv: TextView = itemView.findViewById(R.id.story_title)
//        val hint_tv: TextView = itemView.findViewById(R.id.story_hint)
//        val first_img: ImageView = itemView.findViewById(R.id.first_image)
//        val story_img: ImageView = itemView.findViewById(R.id.story_item_image)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.story_item, parent, false)
//        return StoryViewHolder(view)
//    }
//
//    // 绑定数据
//    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
//        // 获取数据
//        val storyData = getItem(position)
//        holder.title_tv.text = storyData.title
//        holder.hint_tv.text = storyData.hint
//
//        Glide.with(holder.first_img.context)
//            .load(storyData.images.firstOrNull())
//            .placeholder(R.drawable.placeholder) // 设置加载中的占位图
//            .error(R.drawable.error_image) // 设置加载失败的错误图
//            .into(holder.first_img)
//
//        Glide.with(holder.story_img.context)
//            .load(storyData.images)
//            .placeholder(R.drawable.placeholder) // 设置加载中的占位图
//            .error(R.drawable.error_image) // 设置加载失败的错误图
//            .into(holder.story_img)
//    }
//}
