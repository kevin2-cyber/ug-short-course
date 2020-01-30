package io.shortcourse.truchat.view

import android.os.Bundle
import io.codelabs.sdk.util.debugLog
import io.shortcourse.truchat.R
import io.shortcourse.truchat.core.view.RootActivity
import io.shortcourse.truchat.model.Room

class UsersActivity : RootActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)

        if (intent.hasExtra(EXTRA_ROOM)) {
            debugLog(intent.getParcelableExtra<Room>(EXTRA_ROOM))
        }

    }

    companion object {
        const val EXTRA_ROOM = "EXTRA_ROOM"
    }
}