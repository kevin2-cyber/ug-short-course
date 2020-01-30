package io.codelabs.churchinc.core

import androidx.fragment.app.Fragment
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import io.codelabs.churchinc.core.datasource.local.ChurchIncDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

abstract class RootFragment : Fragment() {
    private val app: FirebaseApp by inject { parametersOf(requireActivity().application as ChurchIncApplication) }
    val firestore: FirebaseFirestore by inject()
    val storage: FirebaseStorage by inject()
    val auth: FirebaseAuth by inject()

    // ROOM DATABASE DAO
    val dao: ChurchIncDao by inject { parametersOf(requireActivity().application as ChurchIncApplication) }

    // COROUTINES
    private val job = Job()
    // BACKGROUND TASKS
    val ioScope = CoroutineScope(Dispatchers.IO + job)
    // FOREGROUND TASKS
    val uiScope = CoroutineScope(Dispatchers.Main + job)

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}