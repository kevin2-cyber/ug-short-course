package io.hackernews.shared.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import io.hackernews.shared.base.BaseFragment
import io.hackernews.shared.data.Story
import io.hackernews.shared.database.HackerNewsDatabase
import io.hackernews.shared.databinding.FragmentStoriesBinding
import io.hackernews.shared.debugger
import io.hackernews.shared.ui.StoriesAdapter

/**
 * A simple [Fragment] subclass.
 * Use the [StoriesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StoriesFragment : BaseFragment(), StoriesAdapter.OnStoryClickListener {
    private lateinit var binding: FragmentStoriesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentStoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val storiesAdapter = StoriesAdapter(this)

        binding.run {
            storiesGrid.run {
                adapter = storiesAdapter
            }
        }

        HackerNewsDatabase.getInstance(requireContext()).storyDao().getAllStories()
            .observe(viewLifecycleOwner, Observer { stories ->
                debugger(stories.map { item -> item.kids?.size })
                storiesAdapter.submitList(stories)
            })
    }

    override fun onStoryClick(story: Story) {
        findNavController().navigate(
            StoriesFragmentDirections.actionNavStoriesToNavStoryDetails(
                story
            )
        )
    }
}