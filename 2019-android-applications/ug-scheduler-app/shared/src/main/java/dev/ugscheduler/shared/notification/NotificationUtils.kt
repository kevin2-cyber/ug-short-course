/*
 * Copyright (c) 2019. Designed & developed by Quabynah Codelabs(c). For the love of Android development.
 */

package dev.ugscheduler.shared.notification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.core.app.*
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.work.WorkManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dev.ugscheduler.shared.R
import dev.ugscheduler.shared.datasource.local.LocalDataSource
import dev.ugscheduler.shared.datasource.local.LocalDatabase
import dev.ugscheduler.shared.datasource.remote.RemoteDataSource
import dev.ugscheduler.shared.repository.AppRepository
import dev.ugscheduler.shared.util.BaseActivity
import dev.ugscheduler.shared.util.Constants
import dev.ugscheduler.shared.util.debugger
import dev.ugscheduler.shared.util.prefs.UserSharedPreferences
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import java.util.*

/**
 * Notification handler
 */
class NotificationUtils(private val context: Context) {
    // Checks android version for Oreo
    private val isOreo: Boolean by lazy { Build.VERSION.SDK_INT >= Build.VERSION_CODES.O }

    // NOtification Manager instance
    private val manager by lazy { NotificationManagerCompat.from(context) }

    // Local database instance
    private val db by lazy { LocalDatabase.get(context) }

    // Repository instance
    private val repository by lazy {
        AppRepository(
            RemoteDataSource(FirebaseFirestore.getInstance(), FirebaseAuth.getInstance()),
            LocalDataSource(db),
            UserSharedPreferences.getInstance(context),
            FirebaseStorage.getInstance().reference.child(Constants.BUCKET_NAME),
            WorkManager.getInstance(context)
        )
    }

    // Register all notification channels
    fun registerChannels() {
        createNotificationChannel(AppChannels.MISC_CHANNEL, "For other notifications")
        createNotificationChannel(AppChannels.CHAT_CHANNEL, "For class conversations")
        createNotificationChannel(AppChannels.REFUND_ENROL_CHANNEL, "For refund of fees")
        createNotificationChannel(
            AppChannels.STUDENT_ENROL_CHANNEL,
            "For enrolling for a new course "
        )
        debugger("All channels registered")
    }

    // Create notification channels
    @SuppressLint("NewApi")
    private fun createNotificationChannel(channelId: String, channelDesc: String) {
        if (!isOreo) return
        val channel =
            NotificationChannel(
                channelId,
                channelDesc,
                NotificationManager.IMPORTANCE_HIGH
            )
        channel.apply {
            lightColor = R.color.color_primary
            enableVibration(true)
            vibrationPattern = longArrayOf(300, 400, 300, 400)
            enableLights(true)
            description = channelDesc
            setSound(Settings.System.DEFAULT_NOTIFICATION_URI, audioAttributes)
            with((context.getSystemService(Context.NOTIFICATION_SERVICE)) as NotificationManager) {
                createNotificationChannel(channel)
            }
        }
    }

    // Send message to conversation
    suspend fun sendMessage(
        message: String,
        messageId: String,
        courseId: String,
        uid: String,
        target: Class<out BaseActivity>,
        avatar: String? = null
    ) {
        /**
         * Conversation intent
         */
        val intent = Intent(context, target).apply {
            putExtras(
                bundleOf(
                    "messageId" to messageId,
                    "courseId" to courseId
                )
            )
        }

        /**
         * Pending Intent
         */
        val pendingIntent = TaskStackBuilder.create(context).run {
            // Add the intent, which inflates the back stack
            addNextIntentWithParentStack(intent)
            // Get the PendingIntent containing the entire back stack
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        /**
         * Broadcast intent for replies
         */
        val replyIntent = PendingIntent.getBroadcast(context, 0, Intent(context, target).apply {
            putExtras(
                bundleOf(
                    "messageId" to messageId,
                    "uid" to uid,
                    "courseId" to courseId,
                    "avatar" to avatar
                )
            )
        }, 0)

        /**
         * Remote input
         */
        val replyRemoteInput = RemoteInput.Builder(RM_INPUT_KEY)
            .setLabel(context.getString(R.string.message_hint))
            .build()

        /**
         * Reply action
         */
        val replyAction =
            NotificationCompat.Action.Builder(R.drawable.twotone_reply_24px, "Reply", replyIntent)
                .addRemoteInput(replyRemoteInput)
                .setAllowGeneratedReplies(true)
                .setSemanticAction(NotificationCompat.Action.SEMANTIC_ACTION_REPLY)
                .build()

        /**
         * Get course from database
         */
        val course = withContext(IO) {
            repository.getAllCourses(context, false).filter { it.id == courseId }[0]
        }

        /**
         * Get facilitator from database
         */
        val user = withContext(IO) {
            repository.getFacilitators(false).filter { it.id == course.facilitator }[0]
        }

        /**
         * Setup Person
         */
        val person = Person.Builder()
            .setImportant(true)
            .setKey(uid)
            .setName(user.fullName)
            .setUri(avatar)
            .build()

        /**
         * Use messaging style notification
         */
        val messagingStyle = NotificationCompat.MessagingStyle(person)
            .addMessage(
                NotificationCompat.MessagingStyle.Message(
                    message,
                    Calendar.getInstance().timeInMillis,
                    person
                )
            )
        messagingStyle.isGroupConversation = true
        messagingStyle.conversationTitle = course.name

        /**
         * Create notification
         */
        val notification = NotificationCompat.Builder(context, AppChannels.CHAT_CHANNEL)
            .setSmallIcon(R.drawable.ic_notification_io_logo)
            .setContentTitle(course.id)
            .setContentText(message)
            .setStyle(messagingStyle)
            .setContentIntent(pendingIntent)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setColor(ContextCompat.getColor(context, R.color.color_primary))
            .setAutoCancel(true)
            .setOnlyAlertOnce(false)
            .setGroup(courseId)
            .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_SUMMARY)
            .addAction(replyAction)
            .build()

        /**
         * Send notification
         */
        with(manager) {
            notify(MESSAGE_NOTIFICATION_ID, notification)
        }
    }

    fun sendNotification(title: String, message: String) {
        val notification = NotificationCompat.Builder(context, AppChannels.MISC_CHANNEL)
            .setSmallIcon(R.drawable.ic_notification_io_logo)
            .setContentTitle(title)
            .setStyle(NotificationCompat.BigTextStyle())
            .setContentText(message)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(null)
            .setColor(ContextCompat.getColor(context, R.color.color_primary))
            .build()
        with(manager) {
            notify(MISC_NOTIFICATION_ID, notification)
        }
    }

    fun dismissAllNotifications() = manager.cancelAll()

    fun dismissNotificationById(id: Int) = manager.cancel(id)

    companion object {
        private const val MESSAGE_NOTIFICATION_ID = 125
        private const val MISC_NOTIFICATION_ID = 129
        private const val RM_INPUT_KEY = "key_message_reply"
    }
}

/**
 * Notification Channels
 */
object AppChannels {
    const val STUDENT_ENROL_CHANNEL = "student_enrol_channel"
    const val REFUND_ENROL_CHANNEL = "refund_enrol_channel"
    const val CHAT_CHANNEL = "chat_channel"
    const val MISC_CHANNEL = "misc_channel"
}