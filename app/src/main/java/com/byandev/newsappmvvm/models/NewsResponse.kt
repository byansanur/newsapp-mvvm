package com.byandev.newsappmvvm.models

import com.byandev.newsappmvvm.models.responseNews.Article

data class NewsResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)