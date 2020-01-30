package io.ugshortcourse.voteme.core

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * Base class for all [Fragment]s
 */
abstract class VoteMeBaseFragment constructor() : Fragment() {

    //To be overridden by the children classes
    abstract fun getLayoutId(): Int

    abstract fun onViewCreated(instanceState: Bundle?, v: View, parent: VoteMeBaseActivity)

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        (requireActivity().application as VoteMeApplication).component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        onViewCreated(savedInstanceState, view, requireActivity() as VoteMeBaseActivity)
    }

}