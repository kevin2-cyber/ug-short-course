/*
 * Copyright (c) 2019. Designed & developed by Quabynah Codelabs(c). For the love of Android development.
 */

package dev.ugscheduler.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import dev.ugscheduler.R
import dev.ugscheduler.databinding.SignOutFragmentBinding
import dev.ugscheduler.shared.data.Student
import dev.ugscheduler.shared.datasource.local.StudentDao
import dev.ugscheduler.shared.util.activityViewModelProvider
import dev.ugscheduler.shared.util.clipToCircle
import dev.ugscheduler.shared.util.imageUri
import dev.ugscheduler.shared.util.prefs.UserSharedPreferences
import dev.ugscheduler.shared.util.websiteLink
import dev.ugscheduler.shared.viewmodel.AppViewModel
import dev.ugscheduler.shared.viewmodel.AppViewModelFactory
import org.koin.android.ext.android.get

class SignOutFragment : DialogFragment() {
    private lateinit var binding: SignOutFragmentBinding
    private lateinit var viewModel: AppViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SignOutFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = activityViewModelProvider(AppViewModelFactory(get()))

        val dao: StudentDao = get()
        val prefs: UserSharedPreferences = get()
        dao.getStudent(prefs.uid).observe(viewLifecycleOwner, Observer { student ->
            setupUI(student)
        })
    }

    private fun setupUI(student: Student?) {
        clipToCircle(binding.userAvatar, true)
        imageUri(
            binding.userAvatar,
            student?.avatar?.toUri(),
            resources.getDrawable(R.drawable.ic_default_profile_avatar, null)
        )
        websiteLink(binding.manageAccount, getString(R.string.manage_google_account_url))
        websiteLink(binding.privacyPolicy, getString(R.string.privacy_policy_url))
        websiteLink(binding.termsOfService, getString(R.string.tos_url))
        binding.signOut.setOnClickListener {
            viewModel.logout()
            dismiss()
        }
        binding.email.text = student?.email
        binding.username.text = student?.fullName
    }

    override fun onResume() {
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        super.onResume()
    }
}