package com.example.daily.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.daily.ui.fragment.LongFragment
import com.example.daily.ui.fragment.ShortFragment

class CommentsAdapter(
    fragmentActivity: FragmentActivity,
    private val storyId: Long
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 2 // 0: 长评论, 1: 短评论

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ShortFragment.newInstance(storyId)
            1 -> LongFragment.newInstance(storyId)
            else -> throw IllegalArgumentException("无效的评论页索引: $position")
        }
    }
}