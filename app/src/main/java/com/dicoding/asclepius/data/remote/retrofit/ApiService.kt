package com.dicoding.asclepius.data.remote.retrofit

import retrofit2.http.GET
import retrofit2.http.Query
import com.dicoding.asclepius.data.remote.response.ArticleResponse
import retrofit2.Call

interface ApiService {
    @GET("top-headlines?country=us&category=health")
    fun getNews(@Query("apiKey") apiKey: String): Call<ArticleResponse>
}