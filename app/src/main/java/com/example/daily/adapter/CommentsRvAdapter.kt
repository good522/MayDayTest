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
import com.example.daily.bean.Comment

class CommentsRvAdapter (private val onItemClick: (Long) -> Unit
): androidx.recyclerview.widget.ListAdapter<Comment, CommentsRvAdapter.CommentViewHolder>(
    object : DiffUtil.ItemCallback<Comment>() {
        override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem == newItem
        }
    }
) {
   inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val avatar: ImageView = itemView.findViewById(R.id.author_image)
        val author: TextView = itemView.findViewById(R.id.comment_author)
        val content: TextView = itemView.findViewById(R.id.comment_text)
        val time: TextView = itemView.findViewById(R.id.comment_time)
//        val likes: TextView = itemView.findViewById(R.id.comment_likes)
//        val reply_to: TextView = itemView.findViewById(R.id.comment_reply)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.comment_item, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = getItem(position)
        holder.author.text = comment.author
        holder.content.text = comment.content
//        holder.likes.text = comment.likes.toString()
        holder.time.text = formatTime(comment.time.toLong())

        Glide.with(holder.itemView.context)
            .load(comment.avatar)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.error_image)
            .into(holder.avatar)
    }

    private fun formatTime(timestamp: Long): String {
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault())
        return sdf.format(java.util.Date(timestamp * 1000))
    }
}
