package io.hackernews.shared.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.hackernews.shared.data.Story
import io.hackernews.shared.databinding.ItemStoryBinding

class StoriesAdapter(private val listener: OnStoryClickListener) :
    ListAdapter<Story, StoryViewHolder>(Story.DIFF_UTIL) {

    interface OnStoryClickListener {
        fun onStoryClick(story: Story)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        return StoryViewHolder(
            ItemStoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener { listener.onStoryClick(getItem(position)) }
    }
}

class StoryViewHolder(private val binding: ItemStoryBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(story: Story) {
        binding.story = story
    }

}