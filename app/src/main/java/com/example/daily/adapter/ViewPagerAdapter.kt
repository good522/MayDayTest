package com.example.daily.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.daily.ui.fragment.WebViewFragment
import com.example.daily.viewModel.StoryViewModel

class ViewPagerAdapter (
    activity: FragmentActivity,
    private val storyViewModel: StoryViewModel
) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = storyViewModel.getUrlCount()

    override fun createFragment(position: Int): Fragment {
        val story = storyViewModel.getStoryByPosition(position)
        return if (story != null) {
            WebViewFragment.newInstance(story)
        } else {
            Fragment() // 或者返回一个错误提示 Fragment
        }
    }
}