/*
 * Copyright (c) 2019. Designed & developed by Quabynah Codelabs(c). For the love of Android development.
 */

package dev.ugscheduler.ui.onboarding

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.PagerAdapter
import dev.ugscheduler.R
import dev.ugscheduler.databinding.OnboardingFragmentBinding
import dev.ugscheduler.shared.util.BaseActivity

class OnboardingFragment : Fragment() {
    private lateinit var binding: OnboardingFragmentBinding
    private lateinit var viewModel: OnboardingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = OnboardingFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(OnboardingViewModel::class.java)

        // Setup view pager
        binding.pager.adapter = OnboardingPagerAdapter(requireActivity())
        binding.pageIndicator.setViewPager(binding.pager)

        // Cancel onboarding process
        binding.getStarted.setOnClickListener {
            viewModel.performOnboardingCompletion(requireActivity() as BaseActivity)
        }

        // Move to home page
//        if (!viewModel.showOnboarding(requireActivity() as BaseActivity))
//            (requireActivity() as BaseActivity).intentTo(HomeActivity::class.java, true)
    }

    inner class OnboardingPagerAdapter(private val host: Activity) : PagerAdapter() {
        override fun isViewFromObject(view: View, `object`: Any): Boolean = `object` == view

        override fun getCount(): Int = 2

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val v = getPage(position, container)
            container.addView(v)
            return v
        }

        private fun getPage(position: Int, container: ViewGroup): View {
            return when (position) {
                0 -> {
                    val pageOne = host.layoutInflater.inflate(R.layout.page_one, container, false)
                    pageOne
                }

                else -> {
                    val pageTwo = host.layoutInflater.inflate(R.layout.page_two, container, false)
                    pageTwo
                }
            }
        }
    }
}
