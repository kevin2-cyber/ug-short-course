package io.codelabs.chatapplication.view.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import io.codelabs.chatapplication.R
import io.codelabs.chatapplication.data.BaseDataModel
import io.codelabs.chatapplication.data.User
import io.codelabs.chatapplication.util.*
import io.codelabs.chatapplication.view.ProfileActivity
import io.codelabs.chatapplication.view.adapter.UserAdapter
import kotlinx.android.synthetic.main.fragment_main.*

class UserFragment : BaseFragment(), UserAdapter.ItemClickListener {


    override fun getLayoutId(): Int = R.layout.fragment_main

    override fun onViewCreated(instanceState: Bundle?, view: View, rootActivity: BaseActivity) {
        val adapter = UserAdapter(rootActivity, this)
        grid.adapter = adapter
        val layoutManager = LinearLayoutManager(rootActivity)
        grid.layoutManager = layoutManager
        grid.setHasFixedSize(true)

        try {
            rootActivity.firestore.collection(USER_REF)
                .addSnapshotListener(rootActivity) { snapshot, exception ->
                    if (exception != null) {
                        debugLog("Cause: ${exception.cause}")
                        debugLog(exception.localizedMessage)
                        return@addSnapshotListener
                    }

                    val users = snapshot?.toObjects(User::class.java)
                    val filter = users?.filter { it.key != rootActivity.database.key }
                    if (users != null) adapter.addData(filter!!.toMutableList())

                }
        } catch (e: Exception) {
            debugLog(e.cause)
        }

    }

    override fun onClick(item: BaseDataModel) {
        val bundle = Bundle(0)
        bundle.putParcelable(ProfileActivity.EXTRA_USER, item)
        requireActivity().intentTo(ProfileActivity::class.java, bundle)
    }
}