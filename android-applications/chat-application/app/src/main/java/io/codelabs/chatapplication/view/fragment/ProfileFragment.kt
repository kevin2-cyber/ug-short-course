package io.codelabs.chatapplication.view.fragment

import android.os.Bundle
import android.view.View
import com.bumptech.glide.load.engine.DiskCacheStrategy
import io.codelabs.chatapplication.R
import io.codelabs.chatapplication.data.User
import io.codelabs.chatapplication.glide.GlideApp
import io.codelabs.chatapplication.util.*
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : BaseFragment() {
    override fun getLayoutId(): Int = R.layout.fragment_profile

    override fun onViewCreated(instanceState: Bundle?, view: View, rootActivity: BaseActivity) {
        val key = rootActivity.database.key
        rootActivity.firestore.document(String.format(USER_DOC_REF, key))
            .addSnapshotListener(rootActivity) { snapshot, ex ->
                if (ex != null) {
                    debugLog(ex.localizedMessage)
                    rootActivity.toast("unable to load user's profile")
                    return@addSnapshotListener
                }

                val user = snapshot?.toObject(User::class.java)
                GlideApp.with(rootActivity)
                    .asBitmap()
                    .load(user?.profile)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .circleCrop()
                    .placeholder(R.drawable.avatar_placeholder)
                    .error(R.drawable.avatar_placeholder)
                    .into(avatar)
                user_name.text = user?.name
                user_status.text = user?.status ?: "\"No status available\""

            }

        fab_edit.setOnClickListener {

        }
    }
}