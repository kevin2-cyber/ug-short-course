/*
 * Copyright (c) 2019. Designed & developed by Quabynah Codelabs(c). For the love of Android development.
 */

package dev.ugscheduler.ui.news

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dev.ugscheduler.databinding.NewsFragmentBinding
import dev.ugscheduler.shared.data.News
import dev.ugscheduler.shared.util.activityViewModelProvider
import dev.ugscheduler.shared.util.setupWithAdapter
import dev.ugscheduler.shared.viewmodel.AppViewModel
import dev.ugscheduler.shared.viewmodel.AppViewModelFactory
import dev.ugscheduler.util.MainNavigationFragment
import org.koin.android.ext.android.get
import java.io.InputStreamReader

class NewsFragment : MainNavigationFragment() {
    private lateinit var binding: NewsFragmentBinding
    private val viewModel by lazy {
        activityViewModelProvider<NewsViewModel>(
            NewsViewModelFactory(
                get()
            )
        )
    }
    private val appViewModel by lazy {
        activityViewModelProvider<AppViewModel>(
            AppViewModelFactory(
                get()
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = NewsFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Get adapter instance
        val newsAdapter = NewsAdapter()

        // Get news from source
        viewModel.news.observe(viewLifecycleOwner, Observer { news ->
            binding.recyclerView.setupWithAdapter(newsAdapter)
            //if (news != null) newsAdapter.submitList(news)
            binding.emptyContent.visibility = View.GONE
            newsAdapter.submitList(getSampleNews(requireContext()))
        })
    }

}

// Get sample news from json
fun getSampleNews(context: Context): MutableList<News> =
    Gson().fromJson<List<News>>(
        InputStreamReader(context.assets.open("sample_news.json")),
        object : TypeToken<List<News>>() {}.type
    ).toMutableList()