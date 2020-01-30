package io.codelabs.churchinc.core.modules

import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import io.codelabs.churchinc.core.ChurchIncApplication
import io.codelabs.churchinc.core.datasource.local.ChurchIncDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module

/**
 * Firebase Module
 */
val firebaseModule = module {
    // CREATE A SINGLETON OF FIREBASE APPLICATION
    single { FirebaseApp.initializeApp(androidContext() as ChurchIncApplication)!! }

    // CREATE A SINGLETON OF CLOUD FIRESTORE
    single { FirebaseFirestore.getInstance(get()) }

    // CREATE A SINGLETON OF CLOUD STORAGE
    single { FirebaseStorage.getInstance() }

    // CREATE A SINGLETON OF AUTHENTICATION
    single { FirebaseAuth.getInstance(get()) }
}


/**
 * Room Module
 */
val roomModule = module {
    single { ChurchIncDatabase.getInstance(androidContext() as ChurchIncApplication).dao() }
}