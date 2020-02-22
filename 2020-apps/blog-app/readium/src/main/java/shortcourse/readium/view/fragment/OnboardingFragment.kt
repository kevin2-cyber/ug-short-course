package shortcourse.readium.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.PagerAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import shortcourse.readium.R
import shortcourse.readium.core.viewmodel.OnboardingViewModel
import shortcourse.readium.databinding.FragmentOnboardingBinding
import shortcourse.readium.databinding.OnboardingPageItemLayoutBinding

/**
 * A simple [Fragment] subclass.
 */
class OnboardingFragment : Fragment() {
    private lateinit var binding: FragmentOnboardingBinding
    private val onboardingViewModel by viewModel<OnboardingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.run {

            onboardingViewModel.currentState.observe(viewLifecycleOwner, Observer { shouldShow ->
                if (!shouldShow)
                    findNavController().navigate(OnboardingFragmentDirections.actionNavOnboardingToNavRegistration())
            })

            pager.adapter = OnboardingPagerAdapter()
            indicator.setViewPager(pager)
            executePendingBindings()
        }
    }

    inner class OnboardingPagerAdapter: PagerAdapter() {
        private val inflater by lazy { LayoutInflater.from(requireContext()) }
        private var pageOne: OnboardingPageItemLayoutBinding? = null
        private var pageTwo: OnboardingPageItemLayoutBinding? = null
        private var pageThree: OnboardingPageItemLayoutBinding? = null

        override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

        override fun getCount(): Int = 3

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val v = getPage(container, position)
            container.addView(v)
            return v
        }

        private fun getPage(container: ViewGroup, position: Int): View {
            return when (position) {
                0 -> {
                    if (pageOne == null)
                        pageOne =
                            OnboardingPageItemLayoutBinding.inflate(inflater, container, false)
                    pageOne?.animationView?.setAnimation(R.raw.pencil)
                    pageOne?.pagerTitle?.text = "Create a blog"
                    pageOne?.pagerDesc?.text =
                        "Debet comprehensam sit ea, cu augue admodum tibique sed. Mea ad option audire. Erat audire philosophia his ad. Repudiare voluptaria constituam eam et, ut utroque antiopam usu. An deserunt scribentur vim, nostrud habemus docendi vim in."
                    pageOne!!.root
                }

                1 -> {
                    if (pageTwo == null)
                        pageTwo =
                            OnboardingPageItemLayoutBinding.inflate(inflater, container, false)
                    pageTwo?.animationView?.setAnimation(R.raw.upload)
                    pageTwo?.pagerTitle?.text = "Upload for review"
                    pageTwo?.pagerDesc?.text =
                        "Debet comprehensam sit ea, cu augue admodum tibique sed. Mea ad option audire. Erat audire philosophia his ad. Repudiare voluptaria constituam eam et, ut utroque antiopam usu. An deserunt scribentur vim, nostrud habemus docendi vim in."
                    pageTwo!!.root
                }

                else -> {
                    if (pageThree == null)
                        pageThree =
                            OnboardingPageItemLayoutBinding.inflate(inflater, container, false)
                    pageThree?.animationView?.setAnimation(R.raw.pencil)
                    pageThree?.skipButton?.run {
                        visibility = View.VISIBLE
                        setOnClickListener {
                            onboardingViewModel.completeOnboarding()
                        }
                    }

                    pageThree?.pagerTitle?.text = "Read from other authors"
                    pageThree?.pagerDesc?.text =
                        "Debet comprehensam sit ea, cu augue admodum tibique sed. Mea ad option audire. Erat audire philosophia his ad. Repudiare voluptaria constituam eam et, ut utroque antiopam usu. An deserunt scribentur vim, nostrud habemus docendi vim in."
                    pageThree!!.root
                }
            }
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }

}