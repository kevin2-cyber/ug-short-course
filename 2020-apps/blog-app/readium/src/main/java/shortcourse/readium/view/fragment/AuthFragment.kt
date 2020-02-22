package shortcourse.readium.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import shortcourse.readium.R
import shortcourse.readium.core.base.BaseFragment
import shortcourse.readium.core.util.InputValidator
import shortcourse.readium.core.util.debugger
import shortcourse.readium.core.util.resolveText
import shortcourse.readium.core.util.showSnackbar
import shortcourse.readium.core.viewmodel.AuthViewModel
import shortcourse.readium.databinding.FragmentAuthBinding


/**
 * A simple [Fragment] subclass.
 */
class AuthFragment : BaseFragment() {
    private lateinit var binding: FragmentAuthBinding
    private val controller by lazy { findNavController() }
    private val authViewModel by viewModel<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Observe login state
        authViewModel.authState.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                AuthViewModel.AuthenticationState.CANCELLED -> {
                    debugger("Auth cancelled by user")
                    // controller.popBackStack(R.id.nav_home, true)
                }
                AuthViewModel.AuthenticationState.UNAUTHENTICATED -> {
                    debugger("User has not been authenticated yet")
                }

                AuthViewModel.AuthenticationState.AUTHENTICATED -> {
                    debugger("User signed in successfully")
                    controller.popBackStack()
                }
                AuthViewModel.AuthenticationState.INVALID_AUTHENTICATION -> {
                    binding.run {
                        InputValidator.toggleFields(true, authPwd, authEmail)
                        root.showSnackbar("Authentication failed")
                    }
                }
                AuthViewModel.AuthenticationState.AUTHENTICATING -> {
                    binding.run {
                        InputValidator.toggleFields(false, authPwd, authEmail)
                        root.showSnackbar("Signing in...", true)
                    }
                }

                else -> {
                    /*Do nothing*/
                }
            }
        })

        binding.run {
            navCreateAccount.setOnClickListener {
                controller.navigate(AuthFragmentDirections.actionNavAuthToNavOnboarding())
            }
            performLogin.setOnClickListener {
                if (InputValidator.validateEmail(authEmail)) {
                    if (InputValidator.validatePassword(authPwd)) {
                        authViewModel.authenticateAccount(
                            authEmail.resolveText,
                            authPwd.resolveText
                        )
                    } else root.showSnackbar("Your password is too short")
                } else
                    root.showSnackbar("Please enter a valid email address")
            }
            navUseAsGuest.setOnClickListener { controller.popBackStack(R.id.nav_home, false) }
        }

    }

    override fun onDestroyView() {
        authViewModel.cancelAuthentication()
        super.onDestroyView()
    }
}