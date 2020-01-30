package io.ugshortcourse.voteme.view.fragments

import android.os.Bundle
import android.view.View
import io.ugshortcourse.voteme.R
import io.ugshortcourse.voteme.core.VoteMeBaseActivity
import io.ugshortcourse.voteme.core.VoteMeBaseFragment
import io.ugshortcourse.voteme.core.voteMeLogger

class HomeFragment : VoteMeBaseFragment() {
    override fun getLayoutId(): Int = R.layout.fragment_home

    override fun onViewCreated(instanceState: Bundle?, v: View, parent: VoteMeBaseActivity) {
        voteMeLogger("Fragment Info: ${parent.firestore.app.persistenceKey}")
    }

}