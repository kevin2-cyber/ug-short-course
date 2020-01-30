package dev.codelabs.firebasetestapp

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_other.*
import java.util.*
import kotlin.random.Random

class OtherActivity : AppCompatActivity() {
    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val rtDb: FirebaseDatabase by lazy { FirebaseDatabase.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other)

        push_data.setOnClickListener {
            for (i in 0 until 10) {
                val dummyData = DummyData(UUID.randomUUID().toString())
                db.push(dummyData)
                rtDb.push(dummyData)
            }
        }

        // Adapter
        val adapter = DataAdapter()
        data_list.setupWithAdapter(adapter)

        read_data.setOnClickListener {

            // Real-time database
            rtDb.reference.child("test-data").addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    debugger(p0.message)
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        val dataList = mutableListOf<DummyData?>()
                        p0.children.forEach { snapshot ->
                            val dummyData = snapshot.getValue(DummyData::class.java)
                            dataList.add(dummyData)
                        }
                        adapter.submitList(dataList)
                    } else adapter.submitList(mutableListOf())
                }
            })

            // Firestore
            /*db.collection("test-data")
                .addSnapshotListener(this@OtherActivity) { snapshot, exception ->
                    if (exception != null) {
                        debugger(exception.localizedMessage)
                        return@addSnapshotListener
                    }

                    val dataList = snapshot?.toObjects(DummyData::class.java)
                    adapter.submitList(dataList)
                }*/

        }

        createNotification()

    }

    private fun createNotification() {
        // Create an explicit intent for an Activity in your app
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle("Hello world")
            .setContentText("This is a sample notification")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(200, 300, 400, 500))
            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(Random.nextInt(), builder.build())
        }
    }

}
