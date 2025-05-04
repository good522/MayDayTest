package com.example.daily.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.daily.R
import com.example.daily.bean.Story

class ListAdapter (private val onItemClick: (Long) -> Unit
): androidx.recyclerview.widget.ListAdapter<Story, ListAdapter.LtViewHolder>(
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LtViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_item, parent, false)
        return LtViewHolder(view)
    }

    // 绑定数据
    override fun onBindViewHolder(holder: LtViewHolder, position: Int) {
        // 获取数据
        val rvData = getItem(position)
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
