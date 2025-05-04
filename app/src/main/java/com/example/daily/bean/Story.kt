package com.example.daily.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Story(
    val ga_prefix: String,
    val hint: String,
    val id: Long,
    val image_hue: String,
    val images: List<String>,
    val title: String,
    val type: Int,
    val url: String,
    var isLiked: Boolean = false,
    var isFavorited: Boolean = false
) : Parcelable