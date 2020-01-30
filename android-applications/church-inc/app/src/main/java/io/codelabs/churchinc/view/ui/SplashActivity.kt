package io.codelabs.churchinc.view.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import io.codelabs.churchinc.R
import io.codelabs.churchinc.core.RootActivity

/**
 * This is  the welcome screen of the application
 */
class SplashActivity : RootActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ATTACH A LAYOUT TO THIS ACTIVITY
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            startActivity(
                Intent(
                    this@SplashActivity,
                    if (auth.uid.isNullOrEmpty()) LoginActivity::class.java else HomeActivity::class.java
                )
            )
            finish()
        }, 800)
    }

}