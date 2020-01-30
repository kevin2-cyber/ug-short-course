package dev.csshortcourse.assignment.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import dev.csshortcourse.assignment.R
import dev.csshortcourse.assignment.viewmodel.AppViewModel
import dev.csshortcourse.assignment.viewmodel.Response
import dev.csshortcourse.assignment.viewmodel.debugger
import kotlinx.android.synthetic.main.activity_create_account.*

class CreateAccountActivity : AppCompatActivity() {
    private lateinit var viewModel: AppViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        viewModel = AppViewModel()
    }

    fun createUser(view: View) {
        val username = username_field.text.toString()
        val email = email_field.text.toString()
        val password = password_field.text.toString()
        viewModel.createAccount(username, email, password) { response, user ->
            when (response) {
                Response.COMPLETED -> {
                    debugger(user)
                    startActivity(Intent(this@CreateAccountActivity, HomeActivity::class.java))
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
}
