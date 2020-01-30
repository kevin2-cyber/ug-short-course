/*
 * Copyright (c) 2019. Designed & developed by Quabynah Codelabs(c). For the love of Android development.
 */

package dev.ugscheduler.ui.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Tasks
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import dev.ugscheduler.R
import dev.ugscheduler.databinding.SignInFragmentBinding
import dev.ugscheduler.shared.data.Facilitator
import dev.ugscheduler.shared.data.Student
import dev.ugscheduler.shared.util.activityViewModelProvider
import dev.ugscheduler.shared.util.clipToCircle
import dev.ugscheduler.shared.util.debugger
import dev.ugscheduler.shared.util.prefs.UserSharedPreferences
import dev.ugscheduler.shared.util.websiteLink
import dev.ugscheduler.shared.viewmodel.AppViewModel
import dev.ugscheduler.shared.viewmodel.AppViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get

class SignInFragment : DialogFragment() {

    private lateinit var binding: SignInFragmentBinding
    private val viewModel: AppViewModel by lazy {
        activityViewModelProvider<AppViewModel>(
            AppViewModelFactory(get())
        )
    }

    private val snackbar by lazy {
        Snackbar.make(
            binding.root,
            "Signing you into your account",
            Snackbar.LENGTH_INDEFINITE
        )
    }

    // Google sign in options
    private val gso by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }

    // Google Sign in client
    private val googleClient by lazy { GoogleSignIn.getClient(requireActivity(), gso) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SignInFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Bind views
        clipToCircle(binding.userAvatar, true)
        websiteLink(binding.privacyPolicy, getString(R.string.privacy_policy_url))
        websiteLink(binding.termsOfService, getString(R.string.tos_url))

        // Add click action for login button
        binding.signIn.setOnClickListener {
            with(googleClient) {
                startActivityForResult(signInIntent, RC_AUTH)
            }
        }

        // Observe live login state listener
        val prefs = get<UserSharedPreferences>()
        prefs.liveLoginState.observe(viewLifecycleOwner, Observer { state ->
            if (state) dismiss()
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_AUTH) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    try {
                        val account = GoogleSignIn.getSignedInAccountFromIntent(data)
                            .getResult(ApiException::class.java)
                        // FirebaseAuth instance
                        val auth: FirebaseAuth = get()

                        // Login with credentials on Firebase
                        snackbar.show()
                        val ioScope = CoroutineScope(IO)
                        val uiScope = CoroutineScope(Main)

                        // Background
                        ioScope.launch {
                            // Sign in to Firebase with credentials
                            val authTask = Tasks.await(
                                auth.signInWithCredential(
                                    GoogleAuthProvider.getCredential(
                                        account?.idToken,
                                        null
                                    )
                                )
                            )

                            CoroutineScope(Main).launch {
                                // Verify the returned user information
                                if (authTask.user == null) {
                                    snackbar.setText("Login failed. Please check your internet connection")
                                        .addCallback(object :
                                            BaseTransientBottomBar.BaseCallback<Snackbar?>() {
                                            override fun onDismissed(
                                                transientBottomBar: Snackbar?,
                                                event: Int
                                            ) {
                                                dismiss()
                                            }
                                        }).show()
                                } else {
                                    // Firebase User object
                                    val firebaseUser = authTask.user

                                    // Save user
                                    viewModel.addStudent(firebaseUser?.toStudent())

                                    // Refresh data
                                    viewModel.getCurrentStudent(true)
                                        .observe(viewLifecycleOwner, Observer {
                                            if (it != null) viewModel.addStudent(it)
                                        })
                                }
                            }
                        }
                    } catch (ex: Exception) {
                        debugger(ex.localizedMessage)
                        snackbar.setText("Login failed. Please check your internet connection")
                            .addCallback(object :
                                BaseTransientBottomBar.BaseCallback<Snackbar?>() {
                                override fun onDismissed(
                                    transientBottomBar: Snackbar?,
                                    event: Int
                                ) {
                                    dismiss()
                                }
                            }).show()
                    }
                }

                else -> {
                    debugger("Login was cancelled")
                }
            }
        }
    }

    companion object {
        private const val RC_AUTH = 9
    }

    override fun onResume() {
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        super.onResume()
    }
}


fun FirebaseUser.toStudent() = Student(
    uid,
    System.currentTimeMillis(),
    email!!,
    phoneNumber,
    photoUrl.toString(),
    null,
    displayName,
    null,
    null,
    null,
    null,
    null
)

fun FirebaseUser.toFacilitator() = Facilitator(
    uid,
    System.currentTimeMillis(),
    photoUrl.toString(),
    email!!,
    displayName,
    2.50,
    phoneNumber
)

fun Facilitator.toStudent() = Student(
    id,
    timestamp,
    email,
    phone,
    avatar,
    null,
    fullName,
    null,
    null,
    null,
    null,
    null
)