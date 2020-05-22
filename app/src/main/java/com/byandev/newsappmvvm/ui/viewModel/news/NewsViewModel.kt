package com.byandev.newsappmvvm.ui.viewModel.news

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.byandev.newsappmvvm.NewsApplication
import com.byandev.newsappmvvm.db.repository.NewsRepository
import com.byandev.newsappmvvm.models.NewsResponse
import com.byandev.newsappmvvm.models.responseNews.Article
import com.byandev.newsappmvvm.util.Resources
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

@Suppress("SameParameterValue")
class NewsViewModel(
    app: NewsApplication,
    private val newRepository : NewsRepository
) : AndroidViewModel(app) {

    val breakingNews: MutableLiveData<Resources<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1
    var breakingNewsResponse: NewsResponse? = null

    val searchNews: MutableLiveData<Resources<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    var searchNewsResponse: NewsResponse? = null

    init {
        getBreakingNews("id")
    }

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        safeBreakingNewsCall(countryCode)
//        breakingNews.postValue(Resources.Loading())


    }

    fun searchNews(query: String) = viewModelScope.launch {
        safeSearchNewsCall(query)
//        searchNews.postValue(Resources.Loading())
//        val response = newRepository.searchNews(query, searchNewsPage)
//        searchNews.postValue(handleSearchNewsResponse(response))
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>) : Resources<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {resultResponse ->
                breakingNewsPage++
                if (breakingNewsResponse == null) {
                    breakingNewsResponse = resultResponse
                } else {
                    val oldArticle = breakingNewsResponse?.articles
                    val newsArticle = resultResponse.articles
                    oldArticle?.addAll(newsArticle)
                }
                return Resources.Success(breakingNewsResponse ?: resultResponse)
            }
        }
        return Resources.Error(response.message())
    }


    private fun handleSearchNewsResponse(response: Response<NewsResponse>) : Resources<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {resultResponse ->
                searchNewsPage++
                if (searchNewsResponse == null) {
                    searchNewsResponse = resultResponse
                } else {
                    val oldArticle = searchNewsResponse?.articles
                    val newsArticle = resultResponse.articles
                    oldArticle?.addAll(newsArticle)
                }
                return Resources.Success(searchNewsResponse ?: resultResponse)
            }
        }
        return Resources.Error(response.message())
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        newRepository.upsert(article)
    }

    fun getSavedNews() = newRepository.getSavedNews()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newRepository.deleteArticle(article)
    }

    private suspend fun safeSearchNewsCall(query: String) {
        searchNews.postValue(Resources.Loading())
        try {
            if (hasInternetConnection()) {
                val response = newRepository.searchNews(query, searchNewsPage)
                searchNews.postValue(handleSearchNewsResponse(response))
            } else {
                searchNews.postValue(Resources.Error("No Internet Connections"))
            }

        } catch (t: Throwable) {
            when(t) {
                is IOException -> searchNews.postValue(Resources.Error("Network Failure"))
                else -> searchNews.postValue(Resources.Error("Conversion Error"))
            }
        }
    }

    private suspend fun safeBreakingNewsCall(countryCode: String) {
        breakingNews.postValue(Resources.Loading())
        try {
            if (hasInternetConnection()) {
                val response = newRepository.getBreakingNews(countryCode, breakingNewsPage)
                breakingNews.postValue(handleBreakingNewsResponse(response))
            } else {
                breakingNews.postValue(Resources.Error("No Internet Connections"))
            }

        } catch (t: Throwable) {
            when(t) {
                is IOException -> breakingNews.postValue(Resources.Error("Network Failure"))
                else -> breakingNews.postValue(Resources.Error("Conversion Error"))
            }
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<NewsApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val cpabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                cpabilities.hasTransport(TRANSPORT_WIFI) -> true
                cpabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                cpabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when(type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }

}