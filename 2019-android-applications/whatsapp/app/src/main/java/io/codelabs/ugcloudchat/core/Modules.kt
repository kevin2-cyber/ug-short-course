package io.codelabs.ugcloudchat.core

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.codelabs.ugcloudchat.model.database.UGCloudChatDB
import io.codelabs.ugcloudchat.model.preferences.UserSharedPreferences
import io.codelabs.ugcloudchat.model.provider.LocalContactsProvider
import io.codelabs.ugcloudchat.viewmodel.ChatViewModel
import io.codelabs.ugcloudchat.viewmodel.UserViewModel
import io.codelabs.ugcloudchat.viewmodel.factory.ChatViewModelFactory
import io.codelabs.ugcloudchat.viewmodel.factory.UserViewModelFactory
import io.codelabs.ugcloudchat.viewmodel.repository.ChatRepository
import io.codelabs.ugcloudchat.viewmodel.repository.UserRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Room database module
 */
val roomModule = module {
    single { UGCloudChatDB.getInstance(androidContext()).dao() }
}


/**
 * User settings shared preferences module
 */
val prefsModule = module {
    single { UserSharedPreferences.getInstance(androidContext()) }
}


/**
 * Firebase SDK module
 */
val firebaseModule = module {
    single { FirebaseFirestore.getInstance() }

    single { FirebaseAuth.getInstance() }
}

/**
 * Contact provider module
 */
val providerModule = module {
    single { LocalContactsProvider.getInstance() }
}

val androidModule = module {
    /*Repositories*/
    single { UserRepository.getInstance(get(), get(), get()) }
    single { ChatRepository.getInstance(get(), get(), get()) }

    /*Factories*/
    factory { UserViewModelFactory(androidApplication(), get()) }
    factory { ChatViewModelFactory(androidApplication(), get()) }

    /*ViewModels*/
    viewModel { UserViewModel(androidApplication(), get()) }
    viewModel { ChatViewModel(androidApplication(), get()) }
}