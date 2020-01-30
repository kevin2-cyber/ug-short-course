package io.codelabs.chatapplication.util

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * Base class for all activities
 */
abstract class BaseFragment constructor() : Fragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        debugLog("FragmentID: ${this::class.java.name}")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreated(savedInstanceState, view, requireActivity() as BaseActivity)
    }

    abstract fun getLayoutId(): Int

    abstract fun onViewCreated(instanceState: Bundle?, view: View, rootActivity: BaseActivity)

}