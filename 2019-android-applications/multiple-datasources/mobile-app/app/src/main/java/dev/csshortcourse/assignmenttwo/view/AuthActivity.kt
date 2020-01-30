package dev.csshortcourse.assignmenttwo.view

import android.os.Bundle
import android.text.TextUtils
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import dev.csshortcourse.assignmenttwo.databinding.ActivityAuthBinding
import dev.csshortcourse.assignmenttwo.model.User
import dev.csshortcourse.assignmenttwo.util.*
import dev.csshortcourse.assignmenttwo.view.ui.user.UserViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AuthActivity : BaseActivity() {
    private lateinit var binding: ActivityAuthBinding
    private val viewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Observe login state
        prefs.liveLoginState.observe(this, Observer {
            if (it)
                moveTo(HomeActivity::class.java, true)
        })

        // Toggle button state
        binding.authName.addTextChangedListener { text ->
            binding.login.isEnabled =
                text.toString().isNotEmpty() && !TextUtils.isDigitsOnly(text.toString())
        }

        // Save logged in user
        binding.login.setOnClickListener {
            // Create a new user object
            val user = User(binding.authName.text.toString())

            // Make a call to our view model to login user
            viewModel.login(user) { workState, _ ->
                when (workState) {
                    WorkState.COMPLETED -> {
                        uiScope.launch {
                            binding.loading.gone()
                            moveTo(HomeActivity::class.java, true)
                        }
                    }

                    WorkState.STARTED -> {
                        uiScope.launch {
                            // Hide the content and show loading screen
                            binding.content.gone()
                            binding.loading.visible()
                        }
                    }

                    WorkState.ERROR -> {
                        uiScope.launch {
                            // Hide progress bar
                            binding.loading.gone()
                            binding.errorMessage.visible()

                            // Show error screen for 3 secs
                            delay(3000)
                            // Show content afterwards
                            binding.errorMessage.gone()
                            binding.content.visible()
                        }
                    }
                }
            }
        }
    }
}
