package com.example.daily.bean

data class Latest(
    val date: String,
    val stories: List<Story>,
    val top_stories: List<TopStory>
)