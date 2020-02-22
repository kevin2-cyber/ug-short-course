package shortcourse.readium.core.koin

import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import shortcourse.readium.core.database.ReadiumDatabase
import shortcourse.readium.core.datasource.RemoteDatasource
import shortcourse.readium.core.repository.AccountRepository
import shortcourse.readium.core.repository.AccountRepositoryImpl
import shortcourse.readium.core.repository.PostRepository
import shortcourse.readium.core.repository.PostRepositoryImpl
import shortcourse.readium.core.storage.AccountPrefs
import shortcourse.readium.core.storage.OnboardingPrefs
import shortcourse.readium.core.util.FirebaseUtil
import shortcourse.readium.core.viewmodel.AccountViewModel
import shortcourse.readium.core.viewmodel.AuthViewModel
import shortcourse.readium.core.viewmodel.OnboardingViewModel
import shortcourse.readium.core.viewmodel.PostViewModel

/**
 * Injectable module(s)
 */
@ExperimentalCoroutinesApi
@FlowPreview
val injectables: MutableList<Module>
    get() = mutableListOf(databaseModule, firebaseModule, applicationModule)

private val databaseModule: Module = module {
    single { ReadiumDatabase.getInstance(androidContext()) }

    single { get<ReadiumDatabase>().postDao() }
    single { get<ReadiumDatabase>().commentDao() }
    single { get<ReadiumDatabase>().accountDao() }
}

private val firebaseModule: Module = module {
    single { FirebaseApp.initializeApp(androidApplication()) }

    single { FirebaseStorage.getInstance().reference.child(FirebaseUtil.BUCKET) }
    single { FirebaseFirestore.getInstance() }
    single { FirebaseAuth.getInstance() }
}

@FlowPreview
@ExperimentalCoroutinesApi
private val applicationModule: Module = module {
    single { RemoteDatasource(get(), get()) }

    // Preferences
    single { AccountPrefs.getInstance(androidContext()) }
    single { OnboardingPrefs.getInstance(androidContext()) }

    // Repositories
    single { AccountRepositoryImpl(get(), get(), get(), get()) as AccountRepository }
    single { PostRepositoryImpl(get(), get(), get(), get(), get()) as PostRepository }

    // ViewModels
    viewModel { AuthViewModel(get()) }
    viewModel { PostViewModel(get()) }
    viewModel { AccountViewModel(get()) }
    viewModel { OnboardingViewModel(get()) }
}