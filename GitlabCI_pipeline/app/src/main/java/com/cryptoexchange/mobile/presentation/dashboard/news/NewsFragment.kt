package com.cryptoexchange.mobile.presentation.dashboard.news

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.cryptoexchange.mobile.core.base.BaseFragment
import com.cryptoexchange.mobile.databinding.FragmentNewsBinding
import com.cryptoexchange.mobile.di.FragmentInjector
import com.cryptoexchange.mobile.extensions.popBackStack
import com.cryptoexchange.mobile.presentation.dashboard.news.adapter.NewsAdapter
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class NewsFragment : BaseFragment(), NewsView {

    private lateinit var binding: FragmentNewsBinding
    private lateinit var adapter: NewsAdapter

    @Inject
    @InjectPresenter
    lateinit var presenter: NewsPresenter

    @ProvidePresenter
    fun providePresenter(): NewsPresenter = presenter

    override fun onAttach(context: Context) {
        FragmentInjector.getComponent().inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun setupUI() {
        super.setupUI()

        with(binding) {
            buttonBack.setOnClickListener { presenter.onBackPressed() }
            adapter = NewsAdapter()
            newsRecycler.layoutManager = LinearLayoutManager(requireContext())
            newsRecycler.adapter = adapter
        }
    }

    override fun showNews(news: List<Unit>) {
        adapter.setNews(news)
    }

    override fun navigateBack() {
        popBackStack()
    }
}