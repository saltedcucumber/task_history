package com.cryptoexchange.mobile.presentation.dashboard.news.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cryptoexchange.mobile.databinding.ItemNewsBinding

class NewsAdapter : RecyclerView.Adapter<NewsViewHolder>() {

    private val news = mutableListOf<Unit>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemNewsBinding.inflate(inflater, parent, false)

        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = news.size

    fun setNews(news: List<Unit>) {
        this.news.clear()
        this.news.addAll(news)
        notifyDataSetChanged()
    }
}