package dev.csshortcourse.assignment.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import dev.csshortcourse.assignment.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun loginNow(view: View) {
        // navigate to login screen
        with(Intent(this@MainActivity, LoginActivity::class.java)) {
            startActivity(this)
        }
    }

    fun createAccount(view: View) {
        // navigate to create account screen
        with(Intent(this@MainActivity, CreateAccountActivity::class.java)) {
            startActivity(this)
        }
    }
}
