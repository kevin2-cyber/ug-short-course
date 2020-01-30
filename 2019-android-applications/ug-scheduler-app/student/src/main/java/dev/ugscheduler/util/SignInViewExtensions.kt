/*
 * Copyright (c) 2019. Designed & developed by Quabynah Codelabs(c). For the love of Android development.
 */

package dev.ugscheduler.util


import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.MenuItem
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.core.net.toUri
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import dev.ugscheduler.R
import dev.ugscheduler.shared.data.Student
import dev.ugscheduler.shared.data.isSignedIn
import dev.ugscheduler.shared.util.debugger
import dev.ugscheduler.shared.util.glide.asGlideTarget
import dev.ugscheduler.shared.util.prefs.UserSharedPreferences
import dev.ugscheduler.ui.auth.AuthViewModel


fun Toolbar.setupProfileMenuItem(
    viewModel: AuthViewModel,
    fm: FragmentManager,
    prefs: UserSharedPreferences,
    lifecycleOwner: LifecycleOwner
) {
    inflateMenu(R.menu.profile)
    val searchItem = menu.findItem(R.id.navigation_search) ?: return
    searchItem.setOnMenuItemClickListener {
        findNavController().navigate(R.id.navigation_search)
        true
    }
    val profileItem = menu.findItem(R.id.action_profile) ?: return
    profileItem.setOnMenuItemClickListener {
        viewModel.onProfileClicked(fm, prefs.isLoggedIn)
        true
    }
    val avatarSize = resources.getDimensionPixelSize(R.dimen.nav_account_image_size)
    viewModel.currentUserInfo.observe(lifecycleOwner, Observer {
        debugger("Current student for toolbar icon: $it")
        setProfileContentDescription(profileItem, resources, it)
        val target = profileItem.asGlideTarget(avatarSize)
        setProfileAvatar(context, target, it?.avatar?.toUri())
    })
}

fun setProfileContentDescription(item: MenuItem, res: Resources, user: Student?) {
    val description = if (user?.isSignedIn() == true) {
        res.getString(R.string.a11y_signed_in_content_description, user.fullName)
    } else {
        res.getString(R.string.a11y_signed_out_content_description)
    }
    MenuItemCompat.setContentDescription(item, description)
}

fun setProfileAvatar(
    context: Context,
    target: Target<Drawable>,
    imageUri: Uri?,
    placeholder: Int = R.drawable.ic_default_avatar_3
) {
    // Inflate the drawable for proper tinting
    val placeholderDrawable = AppCompatResources.getDrawable(context, placeholder)
    when (imageUri) {
        null -> {
            context.debugger("Profile Uri is null")
            Glide.with(context)
                .load(placeholderDrawable)
                .apply(RequestOptions.circleCropTransform())
                .into(target)
        }
        else -> {
            context.debugger("Profile Uri is: $imageUri")
            Glide.with(context)
                .load(imageUri)
                .apply(RequestOptions.placeholderOf(placeholderDrawable).circleCrop())
                .into(target)
        }
    }
}
