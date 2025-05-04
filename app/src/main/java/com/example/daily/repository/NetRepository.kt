package com.example.daily.repository


import com.example.daily.bean.Before
import com.example.daily.bean.Comments
import com.example.daily.bean.Latest
import com.example.daily.bean.StoryExtra
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

object NetRepository {
    private var retrofit = Retrofit.Builder()
        .baseUrl("https://news-at.zhihu.com/api/4/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()

    val apiService = retrofit.create(ApiService::class.java)

    interface ApiService {

        // 获取最新新闻
        @GET("news/latest")
        //配合 lifecycleScope.launch 使用协程
        suspend fun getLatest(): Latest

        // 获取指定日期的新闻
        @GET("news/before/{date}")
        //配合 lifecycleScope.launch 使用协程
        suspend fun getBefore(@Path("date") date: String): Before

        // 获取文章的附加信息（例如点赞数等）
        @GET("story-extra/{id}")
        fun getExtra(@Path("id") storyId: Long): StoryExtra

        // 获取文章的长评论
        @GET("story/{id}/long-comments")
        fun getLongComments(@Path("id") storyId: Long): Comments

        // 获取文章的短评论
        @GET("story/{id}/short-comments")
        fun getShortComments(@Path("id") storyId: Long): Comments
    }
}
