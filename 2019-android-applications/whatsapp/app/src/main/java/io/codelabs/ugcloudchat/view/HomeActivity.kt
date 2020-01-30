package io.codelabs.ugcloudchat.view

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import io.codelabs.ugcloudchat.R
import io.codelabs.ugcloudchat.model.WhatsappUser
import io.codelabs.ugcloudchat.util.debugThis
import io.codelabs.ugcloudchat.view.adapter.OnChatItemClickListener
import io.codelabs.ugcloudchat.view.adapter.WhatsappUserAdapter
import io.codelabs.ugcloudchat.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_home.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

class HomeActivity : BaseActivity(),
    OnChatItemClickListener {

    val perms = arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS)
    private lateinit var adapter: WhatsappUserAdapter

    private val userViewModel by viewModel<UserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        adapter = WhatsappUserAdapter(this)
        chat_list.adapter = adapter
        chat_list.layoutManager = LinearLayoutManager(this)

        // Kick off initial load
        getAllContacts()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_logout -> {
                // Sign out user
                auth.signOut()

                /// Navigate back to the login screen
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finishAfterTransition()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun addNewChat(view: View) {
        // Add new chat from the add contact page
        startActivity(Intent(this@HomeActivity, AddContactActivity::class.java))
    }

    @AfterPermissionGranted(RC_CONTACTS)
    private fun getAllContacts() {
        if (EasyPermissions.hasPermissions(this, perms[0])) {
            debugThis("Has contacts read permission")
            userViewModel.users.observe(this@HomeActivity, Observer { users ->
                debugThis("Observing data on ${users?.size ?: 0} users")
                adapter.addChats(users)
            })
        } else {
            EasyPermissions.requestPermissions(
                this,
                String.format(getString(R.string.permission_rationale), "load your contacts"),
                RC_CONTACTS,
                perms[0]
            )
        }
    }

    override fun onChatClick(user: WhatsappUser, id: Long) {
        startActivity(Intent(this@HomeActivity, ChatActivity::class.java).apply {
            putExtra(ChatActivity.ID, id)
            putExtra(ChatActivity.USER, user)
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    companion object {
        private const val RC_CONTACTS = 8
    }

}