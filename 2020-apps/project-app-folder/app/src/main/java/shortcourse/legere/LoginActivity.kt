package shortcourse.legere

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Patterns
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import shortcourse.legere.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        // Bind properties
        binding.run {

            // Listen for changes in the user's input
            // and update the login button respectively
            emailField.addTextChangedListener { content ->
                val userEmail: String? = content?.toString()
                loginButton.isEnabled = !userEmail.isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()
            }
            passwordField
            loginButton.setOnClickListener {
                // TODO: 2/23/20 Sign in with email & password
                println("Hello world")
            }
        }
    }
}