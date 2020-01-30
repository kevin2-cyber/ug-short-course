/*
 * Copyright (c) 2019. Designed & developed by Quabynah Codelabs(c). For the love of Android development.
 */

package dev.ugscheduler.ui.course

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import coil.api.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dev.ugscheduler.R
import dev.ugscheduler.databinding.FragmentCourseDetailsBinding
import dev.ugscheduler.shared.data.Course
import dev.ugscheduler.shared.data.Student
import dev.ugscheduler.shared.util.activityViewModelProvider
import dev.ugscheduler.shared.util.debugger
import dev.ugscheduler.shared.util.deserializer.getCourses
import dev.ugscheduler.shared.util.prefs.UserSharedPreferences
import dev.ugscheduler.shared.util.toast
import dev.ugscheduler.shared.viewmodel.AppViewModel
import dev.ugscheduler.shared.viewmodel.AppViewModelFactory
import dev.ugscheduler.util.MainNavigationFragment
import org.koin.android.ext.android.get

class CourseDetailsFragment : MainNavigationFragment() {
    private lateinit var binding: FragmentCourseDetailsBinding
    private val viewModel: AppViewModel by lazy {
        activityViewModelProvider<AppViewModel>(
            AppViewModelFactory(get())
        )
    }
    private var currentStudent: Student? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCourseDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val currentCourse = arguments?.get("extra_course") as? Course
        bindUI(currentCourse)
    }

    private fun bindUI(course: Course?) {
        debugger("Course details => $course")
        binding.courseName.text = course?.name
        binding.courseDesc.text = course?.desc
        binding.courseSessions.text =
            if (course?.session.isNullOrEmpty()) "Weekends & Evenings" else course?.session

        // Get facilitator
        getFacilitator(course?.facilitator)

        // Get current student
        if (get<UserSharedPreferences>().isLoggedIn) {
            viewModel.getCurrentStudent(false).observe(viewLifecycleOwner, Observer { student ->
                if (student == null) {
                    viewModel.getCurrentStudent(false).removeObservers(viewLifecycleOwner)
                    viewModel.getCurrentStudent(true).observe(viewLifecycleOwner, Observer {
                        currentStudent = it
                    })
                    return@Observer
                }
                currentStudent = student
            })
        }

        binding.enrolCourse.setOnClickListener {
            findNavController().navigate(
                R.id.navigation_enrol, bundleOf(
                    Pair("extra_course", course),
                    Pair("extra_student", currentStudent)
                )
            )
        }

        binding.swipeRefresh.setOnRefreshListener {
            // Fetch live data from the remote data source
            val filter = getCourses(requireContext()).filter { it.id == course?.id }
            binding.swipeRefresh.isRefreshing = false
            if (filter.isNotEmpty() && filter.size == 1) {
                binding.courseName.text = filter[0].name
                binding.courseDesc.text = filter[0].desc
                getFacilitator(filter[0].facilitator, true)
            } else {
                debugger("Unable to fetch courses")
            }
        }
    }

    private fun getFacilitator(facilitator: String?, refresh: Boolean = false) {
        if (facilitator.isNullOrEmpty()) {
            findNavController().popBackStack()
            toast("There's no facilitator for this course")
            return
        }
        viewModel.getFacilitatorById(facilitator, refresh)
            .observe(viewLifecycleOwner, Observer { person ->
                if (person != null) {
                    binding.facilitatorContainer.setOnClickListener {
                        MaterialAlertDialogBuilder(requireContext()).apply {
                            findNavController().navigate(
                                R.id.navigation_facilitator,
                                bundleOf("extra_facilitator" to person)
                            )
                        }
                    }
                    binding.facilitatorImage.load(person.avatar) {
                        transformations(CircleCropTransformation())
                        placeholder(R.drawable.ic_default_avatar_2)
                        error(R.drawable.ic_default_avatar_3)
                        crossfade(true)
                        diskCachePolicy(CachePolicy.ENABLED)
                    }
                    if (!person.avatar.isNullOrEmpty()) {
                        binding.facilitatorImage.setOnClickListener {
                            findNavController().navigate(
                                R.id.navigation_preview,
                                bundleOf("extra_image" to person.avatar)
                            )
                        }
                    }
                    binding.facilitatorName.text = person.fullName
                    binding.courseFacilitatorDesc.text = person.email
                } else {
                    binding.swipeRefresh.isRefreshing = true
                    getFacilitator(facilitator, true)
                }
            })
    }
}
