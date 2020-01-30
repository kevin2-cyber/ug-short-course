package dev.csshortcourse.assignment

import android.app.Application
import com.google.firebase.FirebaseApp
import dev.csshortcourse.assignment.viewmodel.debugger

class Assignment : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize firebase
        FirebaseApp.initializeApp(this).apply {
            debugger("Firebase is connected successfully")
        }
    }
}