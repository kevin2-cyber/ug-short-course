package io.ugshortcourse.voteme.core.injection

import dagger.Component
import io.ugshortcourse.voteme.core.VoteMeApplication
import io.ugshortcourse.voteme.core.VoteMeBaseActivity
import io.ugshortcourse.voteme.core.VoteMeBaseFragment

@VoteMeScope
@Component(modules = [ContextModule::class, VoteMeModule::class, FirebaseModule::class])
interface VoteMeComponent {

    fun inject(app: VoteMeApplication)

    fun inject(activity: VoteMeBaseActivity)

    fun inject(fragment: VoteMeBaseFragment)

}