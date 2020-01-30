package io.codelabs.ugcloudchat.viewmodel.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.google.firebase.firestore.FirebaseFirestore
import io.codelabs.ugcloudchat.model.Chat
import io.codelabs.ugcloudchat.model.database.UGCloudChatDao
import io.codelabs.ugcloudchat.model.preferences.UserSharedPreferences
import kotlinx.coroutines.CoroutineScope

class ChatDataSourceFactory(
    private val db: FirebaseFirestore,
    private val dao: UGCloudChatDao,
    private val recipient: String,
    private val prefs: UserSharedPreferences,
    private val executor: CoroutineScope
) : DataSource.Factory<String, Chat>() {
    val liveDataSource = MutableLiveData<ChatDataSource>()

    override fun create(): DataSource<String, Chat> {
        val dataSource = ChatDataSource(db, dao, recipient, prefs, executor)
        liveDataSource.postValue(dataSource)
        return dataSource
    }
}