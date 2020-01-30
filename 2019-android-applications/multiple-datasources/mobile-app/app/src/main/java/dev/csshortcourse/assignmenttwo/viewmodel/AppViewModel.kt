package dev.csshortcourse.assignmenttwo.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.AndroidViewModel
import dev.csshortcourse.assignmenttwo.util.debugger
import dev.csshortcourse.assignmenttwo.viewmodel.repository.AppRepository

/**
 * ViewModel instance
 */
open class AppViewModel(app: Application) : AndroidViewModel(app) {
    // Create repository instance
    protected val repo: AppRepository by lazy { AppRepository.get(app) }

    // Connectivity Manager
    private val connectivityManager: ConnectivityManager by lazy { app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }

    // Global connection variables
    var isConnected: Boolean = false
    private var monitoring: Boolean = false

    // Callback for network connection request
    private val callback: ConnectivityManager.NetworkCallback =
        object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                isConnected = true
                debugger("Internet connection is currently available")
            }

            override fun onLost(network: Network) {
                isConnected = false
                debugger("There's no internet connection")
            }
        }

    init {
        // Register listener for network connections
        registerNetworkListener()
    }

    private fun registerNetworkListener() {
        monitoring = true
        debugger("Registering network listener")
        try {
            connectivityManager.registerNetworkCallback(
                NetworkRequest.Builder()
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .build()
                , callback
            )
        } catch (e: Exception) {
            debugger("Cannot register network listener")
        }
    }

    private fun unregisterNetworkListener() {
        monitoring = false
        debugger("Unregistering network listener")
        try {
            connectivityManager.unregisterNetworkCallback(callback)
        } catch (e: Exception) {
            debugger("Cannot unregister network listener")
        }
    }

    override fun onCleared() {
        unregisterNetworkListener()
    }

}