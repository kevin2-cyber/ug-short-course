package dev.csshortcourse.assignment.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import dev.csshortcourse.assignment.R
import dev.csshortcourse.assignment.viewmodel.AppViewModel
import dev.csshortcourse.assignment.viewmodel.Response
import dev.csshortcourse.assignment.viewmodel.debugger
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var viewModel: AppViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        viewModel = AppViewModel()
    }

    fun loginUser(view: View) {
        val email = email_field.text.toString()
        val password = password_field.text.toString()
        viewModel.login(email, password) { response, user ->
            when (response) {
                Response.COMPLETED -> {
                    debugger(user)
                    startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                    finish()
                }
                Response.LOADING -> {
                    debugger("Loading started")
                }
                Response.ERROR -> {
                    debugger("An error occurred")
                }
            }
        }
    }

    fun navCreateAccount(view: View) {
        with(Intent(this@LoginActivity, CreateAccountActivity::class.java)) {
            startActivity(this)
        }
    }
}
