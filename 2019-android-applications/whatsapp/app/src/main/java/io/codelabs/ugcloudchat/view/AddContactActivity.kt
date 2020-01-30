package io.codelabs.ugcloudchat.view

import android.os.Bundle
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import io.codelabs.ugcloudchat.R
import io.codelabs.ugcloudchat.model.LocalContact
import io.codelabs.ugcloudchat.model.provider.LocalContactsProvider
import io.codelabs.ugcloudchat.util.debugThis
import io.codelabs.ugcloudchat.view.adapter.LocalUserAdapter
import io.codelabs.ugcloudchat.viewmodel.ContactUserMapper
import io.codelabs.ugcloudchat.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_contact.*
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Add new chats to the current chat list
 */
class AddContactActivity : BaseActivity() {

    private lateinit var adapter: LocalUserAdapter
    private val viewModel by viewModel<UserViewModel>()
    private val contactProvider by inject<LocalContactsProvider>()

    private val mapper by lazy { ContactUserMapper() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)

        // Setup the recyclerview
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        adapter = LocalUserAdapter(object : LocalUserAdapter.OnContactClickListener {
            override fun onContactClicked(contact: LocalContact) {
                ioScope.launch {
                    dao.addSingleUser(mapper.map(contact))

                    uiScope.launch {
                        finishAfterTransition()
                    }
                }
            }
        })

        // Setup recyclerview with adapter & layout manager
        contact_list.adapter = adapter
        contact_list.layoutManager = LinearLayoutManager(this)
        contact_list.itemAnimator = DefaultItemAnimator()

        ioScope.launch {
            contactProvider.getLocalContacts(this@AddContactActivity) { contacts ->
               uiScope.launch {
                   if (contacts != null) adapter.addContacts(contacts) else debugThis("Contacts are empty")
               }
            }
        }
    }

}