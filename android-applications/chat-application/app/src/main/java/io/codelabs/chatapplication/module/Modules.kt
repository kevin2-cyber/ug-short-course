package io.codelabs.chatapplication.module

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import io.codelabs.chatapplication.core.datasource.UserDatabase
import io.codelabs.chatapplication.room.ChatAppDatabase
import io.codelabs.chatapplication.room.factory.UserViewModelFactory
import io.codelabs.chatapplication.room.repository.UserRepository
import io.codelabs.chatapplication.room.viewmodel.UserViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { ChatAppDatabase.getInstance(get()) }
    single { UserDatabase.getInstance(get()) }
}

val roomModule = module {
    single { UserViewModelFactory(get()) }
    single { UserRepository(get()) }
    viewModel { UserViewModel(get()) }
}

val firebaseModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { FirebaseStorage.getInstance() }
    single { FirebaseMessaging.getInstance() }
}