package shortcourse.homepa.helpers

import android.app.Activity
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider
import shortcourse.homepa.R
import shortcourse.homepa.util.debugger

// Callback for authentication
typealias AuthCallback = (result: FirebaseUser?, error: String?) -> Unit

object AuthenticationHelper {

    /**
     * Twitter login
     */
    fun loginWithTwitter(host: Activity, auth: FirebaseAuth, callback: AuthCallback) {
        val provider = OAuthProvider.newBuilder("twitter.com")
        provider.addCustomParameter("lang", "en")
        /*val pendingAuthResult = auth.pendingAuthResult
        pendingAuthResult?.addOnSuccessListener {  }?.addOnFailureListener {  }*/
        auth.startActivityForSignInWithProvider(host, provider.build())
            .addOnSuccessListener { authResult ->
                // User is signed in.
                // IdP data available in
                // authResult.getAdditionalUserInfo().getProfile().
                // The OAuth access token can also be retrieved:
                // authResult.getCredential().getAccessToken().
                // The OAuth secret can be retrieved by calling:
                // authResult.getCredential().getSecret().

                val username = authResult.additionalUserInfo?.username
                callback(auth.currentUser, null)
            }
            .addOnFailureListener {
                callback(null, it.localizedMessage)
            }
    }

    /**
     * Google login
     */
    fun loginWithGoogle(host: Activity, requestCode: Int) {
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(host.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        // Google sign in client
        val googleSignInClient = GoogleSignIn.getClient(host, gso)

        // Perform login
        val signInIntent = googleSignInClient.signInIntent
        host.startActivityForResult(signInIntent, requestCode)
    }

    /**
     * Setup account for user
     */
    fun setupGoogleAuth(host: Activity, auth: FirebaseAuth, data: Intent?, callback: AuthCallback) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            // Google Sign In was successful, authenticate with Firebase
            val acct = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(acct?.idToken, null)
            auth.signInWithCredential(credential)
                .addOnCompleteListener(host) { authTask ->
                    if (authTask.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        debugger("signInWithCredential:success")
                        val user = auth.currentUser
                        callback(user, null)
                    } else {
                        // If sign in fails, display a message to the user.
                        debugger("signInWithCredential:failure")
                        callback(null, "Failed to sign in user. Try again...")
                    }

                    // ...
                }
        } catch (e: ApiException) {
            // Google Sign In failed, update UI appropriately
            debugger(e.localizedMessage)
            callback(null, "Failed to ")
        }
    }

}