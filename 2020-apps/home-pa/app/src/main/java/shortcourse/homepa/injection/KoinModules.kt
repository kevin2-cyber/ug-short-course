package shortcourse.homepa.injection

import com.google.firebase.firestore.FirebaseFirestore
import org.koin.dsl.module

// This contains all Firebase SDKs used in the application
val firebaseModule = module {
    single { FirebaseFirestore.getInstance() }
}