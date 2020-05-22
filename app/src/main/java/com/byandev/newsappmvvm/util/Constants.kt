package com.byandev.newsappmvvm.util

import com.byandev.newsappmvvm.BuildConfig.NewsApiKey

class Constants  {
    companion object {
        const val BASE_URL = "http://newsapi.org/"
        var API_KEY = NewsApiKey
    }
}