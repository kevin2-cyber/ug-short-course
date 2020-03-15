package shortcourse.homepa.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import shortcourse.homepa.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment() {

    private lateinit var binding: FragmentWelcomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.run {
            // Navigate to login page
            loginButton.setOnClickListener {
                findNavController().navigate(WelcomeFragmentDirections.actionNavWelcomeToNavLogin())
            }

            // Navigate to register page
            registerButton.setOnClickListener {
                findNavController().navigate(WelcomeFragmentDirections.actionNavWelcomeToNavRegister())
            }
        }
    }


}