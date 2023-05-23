package com.cryptoexchange.mobile.presentation.dashboard.news

import com.cryptoexchange.mobile.core.base.BasePresenter
import javax.inject.Inject

class NewsPresenter @Inject constructor() : BasePresenter<NewsView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        viewState.showNews(getNews())
    }

    fun onBackPressed() {
        viewState.navigateBack()
    }

    // just for development
    private fun getNews(): List<Unit> {
        val news = mutableListOf<Unit>()

        for (i in startPos..newsSize) {
            news.add(Unit)
        }

        return news
    }

    companion object {
        private const val newsSize = 10
        private const val startPos = 0
    }
}