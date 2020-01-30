package dev.csshortcourse.ugnews.view.recyclerview

import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.csshortcourse.ugnews.databinding.ItemNewsBinding
import dev.csshortcourse.ugnews.model.NewsArticle
import dev.csshortcourse.ugnews.util.browse
import dev.csshortcourse.ugnews.util.gone
import dev.csshortcourse.ugnews.util.layoutInflater
import dev.csshortcourse.ugnews.util.load

class NewsViewHolder(private val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(news: NewsArticle) {
        if (news.image.isNullOrEmpty()) binding.newsImage.gone()
        if (news.author.isNullOrEmpty()) binding.newsAuthor.gone()
        binding.newsDesc.text = news.desc
        binding.newsAuthor.text = news.author
        binding.newsImage.load(news.image)
        binding.newsTitle.text = news.title

        binding.root.setOnClickListener {
            it.context.browse(news.url)
        }
    }
}

class NewsAdapter : ListAdapter<NewsArticle, NewsViewHolder>(NEWS_DIFF) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(ItemNewsBinding.inflate(parent.context.layoutInflater))
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = getItem(position)
        holder.bind(article)
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