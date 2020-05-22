package com.byandev.newsappmvvm.db.repository

import com.byandev.newsappmvvm.api.RetrofitInstance
import com.byandev.newsappmvvm.db.ArticleDatabase
import com.byandev.newsappmvvm.models.responseNews.Article

class NewsRepository (
    val db : ArticleDatabase
) {
    // Handling breaking news

    suspend fun  getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

    suspend fun searchNews(query: String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(query,pageNumber)


    suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)


    fun getSavedNews() = db.getArticleDao().getAllArticles()


    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)
}