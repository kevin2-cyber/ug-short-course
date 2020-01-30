package io.codelabs.churchinc.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import io.codelabs.churchinc.R
import io.codelabs.churchinc.core.RootActivity
import io.codelabs.churchinc.core.RootFragment
import io.codelabs.churchinc.core.datasource.remote.DataCallback
import io.codelabs.churchinc.core.datasource.remote.getSermons
import io.codelabs.churchinc.model.Sermon
import io.codelabs.churchinc.util.toast
import io.codelabs.churchinc.view.recyclerview.adapter.BibleStudiesAdapter
import kotlinx.android.synthetic.main.fragment_recyclerview.*

class BibleStudiesFragment : RootFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*
            In order to use a recyclerview, we need to add a layout manager to that adapter and also set an adapter for it
         */
        content_recyclerview.layoutManager = LinearLayoutManager(requireContext())
        content_recyclerview.setHasFixedSize(true)
        val adapter = BibleStudiesAdapter(requireContext())
        content_recyclerview.adapter = adapter

        (requireActivity() as? RootActivity)?.getSermons(object: DataCallback<MutableList<Sermon>> {
            override fun onError(e: String?) {
                requireContext().toast(e)
            }

            override fun onComplete(result: MutableList<Sermon>?) {
                if (result != null) adapter.addData(result)
            }
        })
    }
}