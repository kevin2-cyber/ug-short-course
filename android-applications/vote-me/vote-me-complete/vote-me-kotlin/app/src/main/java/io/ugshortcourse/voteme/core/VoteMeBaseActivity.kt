package io.ugshortcourse.voteme.core

import android.app.Activity
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import androidx.annotation.IntDef
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import io.ugshortcourse.voteme.core.sharedprefs.VoteMePrefs
import org.jetbrains.anko.connectivityManager
import javax.inject.Inject

/**
 * Base class for all [Activity]s
 */
abstract class VoteMeBaseActivity : AppCompatActivity() {

    @Inject
    lateinit var firestore: FirebaseFirestore
    @Inject
    lateinit var storage: StorageReference
    @Inject
    lateinit var auth: FirebaseAuth
    @Inject
    lateinit var database: VoteMePrefs

    protected var snackbar: Snackbar? = null
    private lateinit var connManager: ConnectivityManager

    //To be overridden by the children classes
    protected abstract val layoutId: Int

    protected abstract fun onViewCreated(instanceState: Bundle?, intent: Intent)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Inject dagger
        (application as VoteMeApplication).component.inject(this)
        setContentView(layoutId)

        connManager = connectivityManager

        //Should be a t the bottom of this function
        onViewCreated(savedInstanceState, intent)
    }

    override fun onStart() {
        super.onStart()
        monitorNetworkState()
    }

    private fun monitorNetworkState() {
        //todo: monitor network state
    }

    //Init snackbar
    protected fun initSnackbar(v: View, msg: String, @SnackbarLengthChecker length: Int) {
        snackbar = Snackbar.make(v, msg, length)
        snackbar?.addCallback(snackbarCallback)
        snackbar?.show()
    }

    // Callback for snackbar
    private val snackbarCallback: BaseTransientBottomBar.BaseCallback<Snackbar> =
        object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                voteMeLogger("Dismissing snackbar")
            }

            override fun onShown(transientBottomBar: Snackbar?) {
                voteMeLogger("Showing snackbar")
                if (getConnectivityState()) removeSnackbar()
            }
        }


    protected fun removeSnackbar() {
        snackbar ?: return
        snackbar?.dismiss()
    }

    //Get the internet connectivity state
    @Synchronized
    protected fun getConnectivityState(): Boolean {
        val networkInfo = connManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    @Retention(AnnotationRetention.RUNTIME)
    @IntDef(Snackbar.LENGTH_INDEFINITE, Snackbar.LENGTH_LONG, Snackbar.LENGTH_SHORT)
    annotation class SnackbarLengthChecker

}