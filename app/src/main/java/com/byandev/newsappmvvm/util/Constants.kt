package com.byandev.newsappmvvm.util

import com.byandev.newsappmvvm.BuildConfig.NewsApiKey

class Constants  {
    companion object {
        const val BASE_URL = "http://newsapi.org/"
        var API_KEY = NewsApiKey
        const val SEARCH_TIME_DELAY = 500L
    }
}