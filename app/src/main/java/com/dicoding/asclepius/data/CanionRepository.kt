package com.dicoding.asclepius.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.asclepius.BuildConfig
import com.dicoding.asclepius.data.local.ResultDao
import com.dicoding.asclepius.data.local.ResultEntity
import com.dicoding.asclepius.data.remote.response.ArticleResponse
import com.dicoding.asclepius.data.remote.response.ArticlesItem
import com.dicoding.asclepius.data.remote.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response

class CanionRepository (
    private val apiService: ApiService,
    private val resultDao: ResultDao
) {
    fun getArticles(): LiveData<Result<List<ArticlesItem>>> {
        val resultLiveData = MutableLiveData<Result<List<ArticlesItem>>>()

        resultLiveData.value = Result.Loading

        apiService.getNews(BuildConfig.AUTH_KEY).enqueue(object : retrofit2.Callback<ArticleResponse> {
            override fun onResponse(
                call: Call<ArticleResponse>,
                response: Response<ArticleResponse>
            ) {
                if (response.isSuccessful) {
                    val articles = response.body()?.articles
                    val newsList = articles?.map { article ->
                        ArticlesItem(
                            article?.title,
                            article?.description,
                            article?.urlToImage,
                        )
                    }
                    resultLiveData.value = Result.Success(newsList ?: emptyList())
                } else {
                    resultLiveData.value = Result.Error("Error fetching articles")
                }
            }

            override fun onFailure(call: Call<ArticleResponse>, t: Throwable) {
                resultLiveData.value = Result.Error(t.message ?: "Unknown Error")
            }
        })

        return resultLiveData
    }

    suspend fun saveResult(result: ResultEntity) {
        withContext(Dispatchers.IO) {
            resultDao.insert(result)
        }
    }

    fun getAllResults(): LiveData<List<ResultEntity>> {
        return resultDao.getAllResultLive()
    }


}