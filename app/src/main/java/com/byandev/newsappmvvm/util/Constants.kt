package com.byandev.newsappmvvm.util

import com.byandev.newsappmvvm.BuildConfig.TokenKey


class Constants  {
    companion object {
        const val BASE_URL = "http://newsapi.org/"
        var API_KEY = TokenKey
        const val SEARCH_TIME_DELAY = 500L
        const val QUERY_PAGE_SIZE = 1
    }
}