package com.byandev.newsappmvvm.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.byandev.newsappmvvm.NewsApplication
import com.byandev.newsappmvvm.R
import com.byandev.newsappmvvm.db.ArticleDatabase
import com.byandev.newsappmvvm.db.repository.NewsRepository
import com.byandev.newsappmvvm.ui.viewModel.news.NewsViewModel
import com.byandev.newsappmvvm.ui.viewModel.news.NewsViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_news.*

class NewsActivity : AppCompatActivity() {

    lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(NewsApplication(), newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)

        bottomNavigationView.setupWithNavController(newsNavHostFragment.findNavController())
    }
}
