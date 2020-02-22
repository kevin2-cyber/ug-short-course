package shortcourse.readium.core.util

import android.view.View
import android.widget.EditText
import com.google.android.gms.tasks.Tasks
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import shortcourse.readium.core.model.account.Account

/**
 * Extracts text from [EditText] field
 */
val EditText.resolveText get() = text.toString()

/**
 * Show a simple [Snackbar]
 */
fun View.showSnackbar(message: String, persisted: Boolean = false, callback: () -> Unit? = {}) =
    Snackbar.make(
        this,
        message,
        if (persisted) Snackbar.LENGTH_INDEFINITE else Snackbar.LENGTH_SHORT
    )
        .addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar?>() {
            override fun onShown(transientBottomBar: Snackbar?) {
                super.onShown(transientBottomBar)

                if (persisted) {
                    GlobalScope.launch(Dispatchers.Main) {
                        // Cancel after 10 secs
                        delay(10000)
                        if (transientBottomBar != null && transientBottomBar.isShown) transientBottomBar.dismiss()
                    }
                }
            }

            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                callback()
            }
        })
        .show()


/**
 * Creates an [Account] instance from [FirebaseUser]
 */

// region FIRESTORE EXTENSIONS

val FirebaseFirestore.posts get() = collection(Entities.POSTS)

val FirebaseFirestore.accounts get() = collection(Entities.ACCOUNTS)

fun DocumentSnapshot.delete() = Tasks.await(this.reference.delete())

fun CollectionReference.deleteAll() {
    val documents = Tasks.await(this.get()).documents
    documents.forEach { snapshot ->
        if (snapshot.exists()) Tasks.await(snapshot.reference.delete())
    }
}

fun FirebaseUser.asAccount(): Account =
    Account("", uid, displayName ?: "", displayName ?: "", email!!, "")
        .copy(id = Account.generateId(displayName ?: uid, displayName ?: ""))

fun FirebaseFirestore.getAccounts() = collection(Entities.ACCOUNTS).get()

fun FirebaseFirestore.getAccountById(id: String) = document("${Entities.ACCOUNTS}/$id").get()

fun FirebaseFirestore.observeAccountById(id: String) = document("${Entities.ACCOUNTS}/$id")

fun FirebaseFirestore.getComments(postId: String) =
    collection("${Entities.POSTS}/$postId/${Entities.COMMENTS}").get()

fun FirebaseFirestore.getPosts() = collection(Entities.POSTS).get()

// endregion FIRESTORE EXTENSIONS