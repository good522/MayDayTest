package com.example.daily.bean

data class Comment(
    val author: String,
    val avatar: String,
    val content: String,
    val id: Int,
    val likes: Int,
//    val reply_to: ReplyTo,
    val time: Long
)