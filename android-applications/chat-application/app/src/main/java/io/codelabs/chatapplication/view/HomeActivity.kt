package io.codelabs.chatapplication.view

import android.animation.AnimatorInflater
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.google.android.material.appbar.AppBarLayout
import io.codelabs.chatapplication.BuildConfig
import io.codelabs.chatapplication.R
import io.codelabs.chatapplication.data.User
import io.codelabs.chatapplication.util.*
import io.codelabs.chatapplication.view.fragment.BlockedUsersFragment
import io.codelabs.chatapplication.view.fragment.ChatFragment
import io.codelabs.chatapplication.view.fragment.ProfileFragment
import io.codelabs.chatapplication.view.fragment.UserFragment
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.coroutines.launch

class HomeActivity(override val layoutId: Int = R.layout.activity_home) : BaseActivity() {
    private lateinit var user: User

    override fun onViewCreated(instanceState: Bundle?, intent: Intent) {
        setSupportActionBar(toolbar)
        toolbar.title = getString(R.string.empty_text)

        // Observe user instance
        userViewModel.getCurrentUser(database.key!!).observe(this@HomeActivity, Observer {
            uiScope.launch {
                if (it != null && it.key.isNotEmpty()) {
                    user = it
                    bindUser()
                }
            }
        })

        // Setup View pager
        setupViewPager(viewpager)
        viewpager.offscreenPageLimit = 1
        tabLayout.setupWithViewPager(viewpager)

        app_bar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            val animatorRes = if (verticalOffset == 0) {
                R.animator.appbar_elevation_disable
            } else {
                R.animator.appbar_elevation_enable
            }
            app_bar.stateListAnimator = AnimatorInflater.loadStateListAnimator(this, animatorRes)
        })
    }

    private fun setupViewPager(viewpager: ViewPager) {
        val res = resources
        val adapter = Adapter(supportFragmentManager).apply {
            addFragment(ChatFragment(), res.getString(R.string.chats))
            addFragment(UserFragment(), res.getString(R.string.all_users))
            addFragment(BlockedUsersFragment(), res.getString(R.string.blocked))
            addFragment(ProfileFragment(), res.getString(R.string.profile))
        }

        viewpager.adapter = adapter
    }

    internal class Adapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        private val fragments = ArrayList<Fragment>()
        private val titles = ArrayList<String>()

        fun addFragment(fragment: Fragment, title: String) {
            fragments.add(fragment)
            titles.add(title)
        }

        override fun getItem(position: Int) = fragments[position]

        override fun getCount() = fragments.size

        override fun getPageTitle(position: Int) = titles[position]
    }

    private fun bindUser() {
        //todo: bind user
        debugLog("Current User: $user")
        toolbar_title.text = String.format("%s (v%d)", getString(R.string.app_name), BuildConfig.VERSION_CODE)
    }

    override fun onStop() {
        postLastSeen()
        super.onStop()
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_signout -> {
                auth.signOut()
                ioScope.launch {
                    userViewModel.delete(user)
                    database.key = ""
                    uiScope.launch {
                        toast("We hope to see you soon @${user.name}")
                        intentTo(MainActivity::class.java, true)
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun postLastSeen() {
        if (database.isLoggedIn && !database.key.isNullOrEmpty()) {
            try {
                firestore.document(String.format(USER_DOC_REF, database.key))
                    .update(
                        mapOf<String, Any?>(
                            "lastSeen" to System.currentTimeMillis(),
                            "online" to false
                        )
                    ).addOnCompleteListener {
                        debugLog("Updated user last seen")
                    }
            } catch (e: Exception) {
                debugLog(e.cause)
                debugLog(e.localizedMessage)
            }
        }
    }

}