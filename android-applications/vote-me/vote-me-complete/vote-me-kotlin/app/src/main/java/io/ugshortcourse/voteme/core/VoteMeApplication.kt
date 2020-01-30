package io.ugshortcourse.voteme.core

import android.app.Application
import com.google.firebase.FirebaseApp
import io.ugshortcourse.voteme.core.injection.*
import javax.inject.Inject

/**
 * [Application] class: Called once the application is started or terminated
 */
class VoteMeApplication : Application() {
    lateinit var component: VoteMeComponent

    @Inject
    lateinit var app: FirebaseApp

    override fun onCreate() {
        super.onCreate()

        component = DaggerVoteMeComponent.builder()
            .contextModule(ContextModule(this))
            .voteMeModule(VoteMeModule())
            .firebaseModule(FirebaseModule())
            .build()

        component.inject(this)

        //Get persistence key
        voteMeLogger(app.persistenceKey)
    }

}