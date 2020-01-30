/*
 * Copyright (c) 2019. Designed & developed by Quabynah Codelabs(c). For the love of Android development.
 */

package dev.ugscheduler.ui.news

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import dev.ugscheduler.R
import dev.ugscheduler.databinding.ItemNewsArticleBinding
import dev.ugscheduler.shared.data.News
import dev.ugscheduler.shared.datasource.local.LocalDatabase
import dev.ugscheduler.shared.util.Constants
import dev.ugscheduler.shared.util.websiteLink
import dev.ugscheduler.shared.util.wrapInQuotes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

class NewsViewHolder(private val binding: ItemNewsArticleBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(news: News) {
        // Navigate to web url
        websiteLink(binding.root, news.url)

        // News binding
        binding.newsTitle.text = news.title
        binding.newsDesc.text = news.desc.wrapInQuotes()

        CoroutineScope(IO).launch {
            // Get author information
            val author = LocalDatabase.get(binding.root.context).facilitatorDao()
                .getFacilitatorByIdAsync(news.author ?: Constants.DEFAULT_AUTHOR)
            CoroutineScope(Main).launch {
                if (author != null) {
                    binding.authorContainer.visibility == View.VISIBLE
                    binding.authorAvatar.load(author.avatar) {
                        transformations(CircleCropTransformation())
                        placeholder(R.drawable.ic_default_avatar_3)
                        error(R.drawable.ic_default_profile_avatar)
                        crossfade(true)
                        diskCachePolicy(CachePolicy.ENABLED)
                        networkCachePolicy(CachePolicy.ENABLED)
                        memoryCachePolicy(CachePolicy.ENABLED)
                    }
                    binding.authorName.text = author.fullName
                    binding.authorDesc.text = author.email
                }
            }
        }
    }

}

private val NEWS_DIFF: DiffUtil.ItemCallback<News> = object : DiffUtil.ItemCallback<News>() {
    override fun areItemsTheSame(oldItem: News, newItem: News): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: News, newItem: News): Boolean = oldItem == newItem
}

class NewsAdapter : ListAdapter<News, NewsViewHolder>(NEWS_DIFF) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(ItemNewsArticleBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}