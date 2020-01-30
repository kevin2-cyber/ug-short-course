package dev.codelabs.samplenewsapp.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.codelabs.samplenewsapp.NewsArticle
import dev.codelabs.samplenewsapp.R
import kotlinx.android.synthetic.main.item_news.view.*

class NewsViewHolder(val v: View) : RecyclerView.ViewHolder(v)

class NewsAdapter : ListAdapter<NewsArticle, NewsViewHolder>(NEWS_DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_news,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val newsArticle = getItem(position)
        holder.v.news_title.text = newsArticle.title
        holder.v.news_desc.text = newsArticle.desc
    }

    companion object {
        private val NEWS_DIFF: DiffUtil.ItemCallback<NewsArticle> =
            object : DiffUtil.ItemCallback<NewsArticle>() {
                override fun areItemsTheSame(oldItem: NewsArticle, newItem: NewsArticle): Boolean =
                    oldItem.url == newItem.url

                override fun areContentsTheSame(
                    oldItem: NewsArticle,
                    newItem: NewsArticle
                ): Boolean = oldItem == newItem
            }
    }
}