package com.byandev.newsappmvvm.db.repository

import com.byandev.newsappmvvm.api.RetrofitInstance
import com.byandev.newsappmvvm.db.ArticleDatabase

class NewsRepository (
    val db : ArticleDatabase
) {
    // Handling breaking news

    suspend fun  getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

}