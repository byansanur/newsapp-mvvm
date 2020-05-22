package com.byandev.newsappmvvm.ui.viewModel.News

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byandev.newsappmvvm.db.repository.NewsRepository
import com.byandev.newsappmvvm.models.NewsResponse
import com.byandev.newsappmvvm.models.responseNews.Article
import com.byandev.newsappmvvm.util.Resources
import kotlinx.coroutines.launch
import retrofit2.Response

@Suppress("SameParameterValue")
class NewsViewModel(
    private val newRepository : NewsRepository
) :ViewModel() {

    val breakingNews: MutableLiveData<Resources<NewsResponse>> = MutableLiveData()
    private var breakingNewsPage = 1

    val searchNews: MutableLiveData<Resources<NewsResponse>> = MutableLiveData()
    private var searchNewsPage = 1

    init {
        getBreakingNews("id")
    }

    private fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        breakingNews.postValue(Resources.Loading())
        val response = newRepository.getBreakingNews(countryCode, breakingNewsPage)
        breakingNews.postValue(handleBreakingNewsResponse(response))

    }

    fun searchNews(query: String) = viewModelScope.launch {
        searchNews.postValue(Resources.Loading())
        val response = newRepository.searchNews(query, searchNewsPage)
        searchNews.postValue(handleSearchNewsResponse(response))
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>) : Resources<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {resultResponse ->
                return Resources.Success(resultResponse)
            }
        }
        return Resources.Error(response.message())
    }


    private fun handleSearchNewsResponse(response: Response<NewsResponse>) : Resources<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {resultResponse ->
                return Resources.Success(resultResponse)
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


}