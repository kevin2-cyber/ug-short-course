package shortcourse.readium.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import shortcourse.readium.R
import shortcourse.readium.core.util.InputValidator
import shortcourse.readium.core.util.debugger
import shortcourse.readium.core.util.resolveText
import shortcourse.readium.core.util.showSnackbar
import shortcourse.readium.core.viewmodel.AuthViewModel
import shortcourse.readium.databinding.FragmentRegistrationBinding

/**
 * A simple [Fragment] subclass.
 */
class RegistrationFragment : Fragment() {
    private lateinit var binding: FragmentRegistrationBinding
    private val controller by lazy { findNavController() }
    private val authViewModel by viewModel<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Observe login state
        authViewModel.authState.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                AuthViewModel.AuthenticationState.UNAUTHENTICATED -> {
                    debugger("User has not been authenticated yet")
                }

                AuthViewModel.AuthenticationState.AUTHENTICATED -> {
                    controller.popBackStack(R.id.nav_home, true)
                }

                AuthViewModel.AuthenticationState.INVALID_AUTHENTICATION -> {
                    binding.run {
                        InputValidator.toggleFields(
                            true,
                            authPwd,
                            authEmail,
                            authLastName,
                            authFirstName
                        )
                        root.showSnackbar("Authentication failed")
                    }
                }

                AuthViewModel.AuthenticationState.AUTHENTICATING -> {
                    binding.run {
                        InputValidator.toggleFields(
                            false,
                            authPwd,
                            authEmail,
                            authFirstName,
                            authLastName
                        )
                        root.showSnackbar("Signing in...", true)
                    }
                }

                else -> {
                    /*Do nothing*/
                }
            }
        })

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    authViewModel.cancelAuthentication()
                    findNavController().popBackStack(R.id.nav_auth, true)
                }
            })

        binding.run {
            navCreateAccount.setOnClickListener {
                when {
                    !InputValidator.validateEmail(authEmail) -> root.showSnackbar("Please enter a valid email address")
                    !InputValidator.validatePassword(authPwd) -> root.showSnackbar("Password is too short")
                    !InputValidator.validateFields(
                        authFirstName,
                        authLastName
                    ) -> root.showSnackbar("Check your first name & last name")
                    else -> authViewModel.createAccount(
                        authEmail.resolveText,
                        authPwd.resolveText,
                        authFirstName.resolveText,
                        authLastName.resolveText
                    )
                }
            }
        }

    }

}