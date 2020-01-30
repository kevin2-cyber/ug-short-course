package io.ugshortcourse.voteme.core.injection

import android.content.Context
import dagger.Module
import dagger.Provides
import io.ugshortcourse.voteme.core.VoteMeApplication

/**
 * [Module] for [android.app.Application] [Context] scope
 */
@Module
class ContextModule constructor(private val app: VoteMeApplication) {

    @VoteMeScope
    @Provides
    fun provideContext(): Context = app.applicationContext
}