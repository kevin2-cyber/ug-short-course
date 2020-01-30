package io.codelabs.ugcloudchat.model.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import io.codelabs.ugcloudchat.model.WhatsappUser
import io.codelabs.ugcloudchat.model.database.UGCloudChatDB
import io.codelabs.ugcloudchat.model.provider.LocalContactsProvider
import io.codelabs.ugcloudchat.util.UGCloudChatConstants
import io.codelabs.ugcloudchat.util.debugThis
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Worker to load all contacts on the user's device and compare with the online data set
 */
class LocalContactWorker(private val context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    private val firestore by lazy { FirebaseFirestore.getInstance() }
    private val dao by lazy { UGCloudChatDB.getInstance(context).dao() }
    private val contactProvider by lazy { LocalContactsProvider.getInstance() }

    init {
        debugThis("LocalContactWorker started: ID${params.id}  & tags ${params.tags}")
    }

    private suspend fun getAndStoreContacts() = withContext(Dispatchers.IO) {
        try {
            // Get all local contacts
            contactProvider.getLocalContacts(context) { contacts ->
                debugThis("Get local contacts returned a list of ${contacts?.size ?: 0} contacts")

                if (contacts != null) {
                    // Get all online users
                    val users = Tasks.await(
                        firestore.collection(UGCloudChatConstants.USERS_COLLECTION)
                            .orderBy("timestamp", Query.Direction.DESCENDING)
                            .get()
                    ).toObjects(WhatsappUser::class.java)

                    debugThis("User requested from remote database returned: ${users.size} users")

                    users.forEach { whatsappUser ->
                        // Whether or not to add the user
                        var add = true

                        for (i in 0 until contacts.size) {
                            add = when {

                                //Ignore all users without phone numbers online or offline
                                whatsappUser.phone.isNullOrEmpty() || contacts[i].phone.isNullOrEmpty() -> false

                                // Add contact to local list if phone number matched the one online
                                whatsappUser.phone.contains(contacts[i].phone!!, true) -> true

                                // Else, just return false
                                else -> false
                            }
                        }

                        if (add) {
                            // Add user who currently matches the criteria above
                            dao.addSingleUser(whatsappUser)
                        }

                    }

                } else debugThis("Could not retrieve contacts")

            }
        } catch (ex: Exception) {
            // Catch any errors that may occur
            debugThis("An error occurred while retrieving your contacts' list: ${ex.localizedMessage}")
        }
    }

    override suspend fun doWork(): Result {
        getAndStoreContacts()
        return Result.success()
    }
}