package io.codelabs.chatapplication.view

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import io.codelabs.chatapplication.R
import io.codelabs.chatapplication.util.BaseActivity
import io.codelabs.chatapplication.util.intentTo
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch

class MainActivity(override val layoutId: Int = R.layout.activity_main) : BaseActivity() {

    override fun onViewCreated(instanceState: Bundle?, intent: Intent) {

        if (database.isLoggedIn && !database.key.isNullOrEmpty()) {
            userViewModel.getCurrentUser(database.key!!).observe(this@MainActivity, Observer {
                uiScope.launch {
                    if (it != null && it.key.isNotEmpty()) {
                        get_started.text = String.format("Continue as %s", it.name)
                        get_started.setOnClickListener {
                            intentTo(HomeActivity::class.java, true)
                        }
                    }
                }
            })
        } else {
            get_started.setOnClickListener {
                intentTo(AuthActivity::class.java, true)
            }
        }

    }

}
