/*
 * Copyright (c) 2019. Designed & developed by Quabynah Codelabs(c). For the love of Android development.
 */

package dev.ugscheduler.ui.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updatePaddingRelative
import androidx.lifecycle.ViewModelProvider
import dev.ugscheduler.BuildConfig
import dev.ugscheduler.databinding.AboutFragmentBinding
import dev.ugscheduler.shared.util.debugger
import dev.ugscheduler.shared.util.doOnApplyWindowInsets
import dev.ugscheduler.shared.util.setupWithAdapter
import dev.ugscheduler.util.MainNavigationFragment

class AboutFragment : MainNavigationFragment() {

    private lateinit var binding: AboutFragmentBinding
    private lateinit var viewModel: AboutViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AboutFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AboutViewModel::class.java)

        binding.appVersion.text = String.format("v%s", BuildConfig.VERSION_NAME)

        val adapter = AboutLibsAdapter()
        val libs = LibraryDeserializer.deserialize(requireContext())
        adapter.submitList(libs)
        binding.libsList.setupWithAdapter(adapter)

        // Padding at the bottom of the list
        binding.libsList.doOnApplyWindowInsets { v, insets, padding ->
            v.updatePaddingRelative(bottom = padding.bottom + insets.systemWindowInsetBottom)
        }
    }

}
