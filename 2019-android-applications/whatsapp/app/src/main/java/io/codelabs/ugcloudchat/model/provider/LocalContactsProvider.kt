package io.codelabs.ugcloudchat.model.provider

import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import io.codelabs.ugcloudchat.model.LocalContact
import io.codelabs.ugcloudchat.util.Callback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Loader class for all contacts stored on a user's device
 */
class LocalContactsProvider private constructor() {

    /**
     * Returns all the local contacts on the user's phone
     */
    suspend fun getLocalContacts(
        context: Context,
        callback: Callback<MutableList<LocalContact>?>
    ) = withContext(Dispatchers.IO) {
        val contactsList = mutableListOf<LocalContact>()

        val cursor: Cursor? = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null
        )

        cursor?.apply {
            while (moveToNext()) {
                val id =
                    getLong(getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID))

                var phone =
                    getString(getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                // Cleanup the phone number
                phone = phone.replace("(", "")
                    .replace("-", "")
                    .replace(")", "")
                    .replace(" ", "")

                val defaultISO = "+233"
                if (!phone.startsWith(defaultISO)) {
                    if (phone.startsWith("0")) {
                        phone = "$defaultISO${phone.substring(1)}"
                    }
                }

                val displayName =
                    getString(getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))

                val lookupKey =
                    getString(getColumnIndex(ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY))

                val photoUri =
                    getString(getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI))

                val thumbNailUri =
                    getString(getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI))

                // Create new local contact
                val contact =
                    LocalContact(
                        id,
                        phone,
                        displayName,
                        lookupKey,
                        photoUri,
                        thumbNailUri
                    )

                // Add local contact to the list
                contactsList.add(contact)
            }

            // Close cursor when done
            close()
        }

        // Return callback
        callback(contactsList)
    }


    companion object {
        @Volatile
        private var instance: LocalContactsProvider? = null

        /**
         * Get singleton instance of the local contacts provider class
         */
        fun getInstance(): LocalContactsProvider = instance
            ?: synchronized(this) {
            instance
                ?: LocalContactsProvider().also { instance = it }
        }
    }
}