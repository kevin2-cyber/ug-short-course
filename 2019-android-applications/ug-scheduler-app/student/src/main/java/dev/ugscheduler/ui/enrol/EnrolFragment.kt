/*
 * Copyright (c) 2019. Designed & developed by Quabynah Codelabs(c). For the love of Android development.
 */

package dev.ugscheduler.ui.enrol

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dev.ugscheduler.databinding.FragmentEnrolBinding
import dev.ugscheduler.shared.data.Course
import dev.ugscheduler.shared.data.Student
import dev.ugscheduler.shared.util.activityViewModelProvider
import dev.ugscheduler.shared.util.debugger
import dev.ugscheduler.shared.util.toast
import dev.ugscheduler.shared.viewmodel.AppViewModel
import dev.ugscheduler.shared.viewmodel.AppViewModelFactory
import dev.ugscheduler.util.MainNavigationFragment
import org.koin.android.ext.android.get
import java.util.*

class EnrolFragment : MainNavigationFragment() {
//    private val args: FragmentEnrolArgs by navArgs()
    private lateinit var binding: FragmentEnrolBinding
    private val viewModel by lazy {
        activityViewModelProvider<AppViewModel>(
            AppViewModelFactory(get())
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEnrolBinding.inflate(inflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val course = arguments?.get("extra_course") as? Course
        val student = arguments?.get("extra_student") as? Student
        if (course != null && student != null) {
            bindUI(course, student)
        } else {
            toast("Cannot setup enrolment. An error occurred")
            findNavController().popBackStack()
        }
    }

    // Bind details for the course
    private fun bindUI(course: Course, student: Student) {
        // todo: enable enrolment of student for course

    }


}
