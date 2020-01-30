package io.ugshortcourse.voteme.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import io.ugshortcourse.voteme.R
import io.ugshortcourse.voteme.core.NO_INTERNET_PROMPT
import io.ugshortcourse.voteme.core.VoteMeBaseActivity
import io.ugshortcourse.voteme.core.addFragment
import io.ugshortcourse.voteme.core.intentTo
import io.ugshortcourse.voteme.view.fragments.ElectionsFragment
import io.ugshortcourse.voteme.view.fragments.HomeFragment
import io.ugshortcourse.voteme.view.fragments.NewsFeedFragment
import io.ugshortcourse.voteme.view.fragments.VoterInfoFragment
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity(override val layoutId: Int = R.layout.activity_home) : VoteMeBaseActivity() {

    private lateinit var fm: FragmentManager

    //Listener for change in click events for the bottom navigation view
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                addFragment(fm, R.id.frame_home, HomeFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
                addFragment(fm, R.id.frame_home, VoterInfoFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_feeds -> {
                addFragment(fm, R.id.frame_home, NewsFeedFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_elections -> {
                addFragment(fm, R.id.frame_home, ElectionsFragment())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onViewCreated(instanceState: Bundle?, intent: Intent) {
        // Init fragment manager
        fm = supportFragmentManager

        //Default fragment
        addFragment(fm, R.id.frame_home, HomeFragment())

        //Add bottom navigation click listener
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        container.setOnClickListener {
            intentTo(CategoryActivity::class.java)
        }
    }

    override fun onEnterAnimationComplete() {
        if (!getConnectivityState()) initSnackbar(container, NO_INTERNET_PROMPT, Snackbar.LENGTH_INDEFINITE)
    }

}
