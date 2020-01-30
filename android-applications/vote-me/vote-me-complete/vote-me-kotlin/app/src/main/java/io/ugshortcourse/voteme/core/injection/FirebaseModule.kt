package io.ugshortcourse.voteme.core.injection

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.Module
import dagger.Provides
import javax.inject.Inject

@Module(includes = [ContextModule::class])
class FirebaseModule {

    @Inject
    @Provides
    @VoteMeScope
    fun provideFirebaseApp(context: Context): FirebaseApp = FirebaseApp.initializeApp(context)!!

    @Provides
    @VoteMeScope
    fun provideAuth(app: FirebaseApp): FirebaseAuth = FirebaseAuth.getInstance(app)

    @Provides
    @VoteMeScope
    fun provideFirestoreSettings(): FirebaseFirestoreSettings =
        FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .setTimestampsInSnapshotsEnabled(true)
            .build()

    @Provides
    @VoteMeScope
    fun provideFirestore(app: FirebaseApp, settings: FirebaseFirestoreSettings): FirebaseFirestore {
        val firestore = FirebaseFirestore.getInstance(app)
        firestore.firestoreSettings = settings
        return firestore
    }

    @Provides
    @VoteMeScope
    fun provideStorage(app: FirebaseApp): StorageReference = FirebaseStorage.getInstance(app).reference

    @Provides
    @VoteMeScope
    fun provideMessaging(): FirebaseMessaging = FirebaseMessaging.getInstance()

}