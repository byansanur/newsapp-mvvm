package com.byandev.newsappmvvm.ui.viewModel.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.byandev.newsappmvvm.NewsApplication
import com.byandev.newsappmvvm.db.repository.NewsRepository

@Suppress("UNCHECKED_CAST")
class NewsViewModelProviderFactory(
    val app: NewsApplication,
    val newsRepository: NewsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewsViewModel(app, newsRepository) as T
    }
}