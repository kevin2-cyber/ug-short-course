/*
 * Copyright (c) 2019. Designed & developed by Quabynah Codelabs(c). For the love of Android development.
 */

package dev.ugscheduler.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import dev.ugscheduler.databinding.FragmentProfileBinding
import dev.ugscheduler.shared.data.Facilitator
import dev.ugscheduler.shared.util.activityViewModelProvider
import dev.ugscheduler.shared.util.debugger
import dev.ugscheduler.shared.viewmodel.AppViewModel
import dev.ugscheduler.shared.viewmodel.AppViewModelFactory
import org.koin.android.ext.android.get
import coil.api.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import dev.ugscheduler.R

class ProfileFragment : DialogFragment() {
    private lateinit var binding: FragmentProfileBinding
    private val viewModel: AppViewModel by lazy {
        activityViewModelProvider<AppViewModel>(
            AppViewModelFactory(get())
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val facilitator = arguments?.get("extra_facilitator") as? Facilitator
        if (facilitator != null) bindUI(facilitator) else findNavController().popBackStack()
    }

    private fun bindUI(facilitator: Facilitator) {
        debugger("Current facilitator => $facilitator")

        binding.username.text = facilitator.fullName
        binding.email.text = facilitator.email
        binding.userAvatar.load(facilitator.avatar) {
            transformations(CircleCropTransformation())
            placeholder(R.drawable.ic_default_avatar_1)
            error(R.drawable.ic_default_avatar_3)
            crossfade(true)
            lifecycle(viewLifecycleOwner)
            diskCachePolicy(CachePolicy.ENABLED)
        }

    }

    override fun onResume() {
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        super.onResume()
    }

}
