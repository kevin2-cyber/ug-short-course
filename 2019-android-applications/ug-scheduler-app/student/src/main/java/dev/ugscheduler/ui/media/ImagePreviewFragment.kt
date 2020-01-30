/*
 * Copyright (c) 2019. Designed & developed by Quabynah Codelabs(c). For the love of Android development.
 */

package dev.ugscheduler.ui.media

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import coil.api.load
import coil.request.CachePolicy
import dev.ugscheduler.R
import dev.ugscheduler.databinding.ImagePreviewFragmentBinding
import dev.ugscheduler.util.MainNavigationFragment

class ImagePreviewFragment : MainNavigationFragment() {
    private lateinit var binding: ImagePreviewFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ImagePreviewFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Get image uri
        val imageUri = arguments?.get("extra_image")?.toString()

        if (!imageUri.isNullOrEmpty()) {
            binding.preview.load(imageUri) {
                diskCachePolicy(CachePolicy.ENABLED)
                crossfade(true)
                placeholder(R.color.content_placeholder)
                error(R.color.content_placeholder)
            }
        } else findNavController().popBackStack()
    }
}