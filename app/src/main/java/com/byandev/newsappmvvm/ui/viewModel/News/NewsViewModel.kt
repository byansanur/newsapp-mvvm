package com.byandev.newsappmvvm.ui.viewModel.News

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byandev.newsappmvvm.db.repository.NewsRepository
import com.byandev.newsappmvvm.models.NewsResponse
import com.byandev.newsappmvvm.util.Resources
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(
    val newRepository : NewsRepository
) :ViewModel() {

    val breakingNews: MutableLiveData<Resources<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1

    init {
        getBreakingNews("id")
    }

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        breakingNews.postValue(Resources.Loading())
        val response = newRepository.getBreakingNews(countryCode, breakingNewsPage)
        breakingNews.postValue(handleBreakingNewsResponse(response))

    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>) : Resources<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {resultResponse ->
                return Resources.Success(resultResponse)
            }
        }
        return Resources.Error(response.message())
    }


}