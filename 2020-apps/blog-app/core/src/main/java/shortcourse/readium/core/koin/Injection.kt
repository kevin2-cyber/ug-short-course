package shortcourse.readium.core.koin

import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module
import shortcourse.readium.core.database.ReadiumDatabase
import shortcourse.readium.core.datasource.RemoteDatasource
import shortcourse.readium.core.storage.AccountPrefs
import shortcourse.readium.core.storage.OnboardingPrefs
import shortcourse.readium.core.util.FirebaseUtil

/**
 * Injectable module(s)
 */
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

private val applicationModule: Module = module {
    single { RemoteDatasource(get(), get()) }

    single { AccountPrefs.getInstance(androidContext()) }
    single { OnboardingPrefs.getInstance(androidContext()) }
}