package io.ugshortcourse.voteme.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import io.ugshortcourse.voteme.R
import io.ugshortcourse.voteme.core.VoteMeBaseActivity

/**
 * [android.app.Application] main entrance
 */
class MainActivity(override val layoutId: Int = R.layout.activity_main) : VoteMeBaseActivity() {

    override fun onViewCreated(instanceState: Bundle?, intent: Intent) {
        Handler().postDelayed({
            startActivity(Intent(this@MainActivity, if(database.isLoggedIn) HomeActivity::class.java else LoginActivity::class.java))
            finishAfterTransition()
        }, 1000)
    }
}
