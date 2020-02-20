package shortcourse.readium.core.repository

import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import shortcourse.readium.core.database.AccountDao
import shortcourse.readium.core.model.account.Account
import shortcourse.readium.core.storage.AccountPrefs
import shortcourse.readium.core.util.accounts
import shortcourse.readium.core.util.asAccount
import shortcourse.readium.core.util.getAccountById

/**
 * Base account repository
 */
interface AccountRepository : Repository {

    suspend fun authenticate(
        method: AuthMethod,
        loginRequest: AuthRequest,
        callback: AuthCallback<Account>
    )

    suspend fun logout()

    suspend fun fetchAccounts(): Flow<MutableList<Account>> {
        TODO("Fetch all accounts")
    }

    suspend fun fetchAccountById(id: String): Flow<Account?> {
        TODO("Fetch account by id")
    }
}

/**
 * [Repository] for [Account] transactions
 */
class AccountRepositoryImpl(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val prefs: AccountPrefs,
    private val accountDao: AccountDao
) : AccountRepository {

    override suspend fun logout() {
        auth.signOut()
        prefs.logout()
    }

    /**
     * Performs authentication of user account based on [method] and [loginRequest]
     * This method provides a [callback] containing the `result` & an optional `errorMessage`
     */
    override suspend fun authenticate(
        method: AuthMethod,
        loginRequest: AuthRequest,
        callback: AuthCallback<Account>
    ) {
        when (method) {
            AuthMethod.EMAIL_PASSWORD -> {
                if (loginRequest is AuthRequest.LoginRequest) {
                    val user = withContext(Dispatchers.IO) {
                        if (loginRequest.isNewUser) Tasks.await(
                            auth.createUserWithEmailAndPassword(
                                loginRequest.email,
                                loginRequest.password
                            )
                        ).user
                        else
                            Tasks.await(
                                auth.signInWithEmailAndPassword(
                                    loginRequest.email,
                                    loginRequest.password
                                )
                            ).user
                    }

                    // Could not sign in user
                    if (user == null) {
                        callback(null, "Unable to sign in user")
                        return
                    }

                    if (loginRequest.isNewUser) {
                        // Get user information
                        val account = user.asAccount()

                        // Save data in remote database
                        withContext(Dispatchers.IO) {
                            Tasks.await(
                                firestore.accounts.document(user.uid).set(
                                    account,
                                    SetOptions.merge()
                                )
                            )
                        }

                        // Save in DAO
                        withContext(Dispatchers.IO) {
                            accountDao.insert(account)
                        }

                        // Store locally
                        prefs.login(account)

                        // Set callback
                        callback(account, null)

                    } else {

                        // Get account information for user
                        val account = withContext(Dispatchers.IO) {
                            Tasks.await(firestore.getAccountById(user.uid)).toObject<Account>()
                                ?: user.asAccount()
                        }

                        // Save in DAO
                        withContext(Dispatchers.IO) {
                            accountDao.insert(account)
                        }

                        // Store locally
                        prefs.login(account)

                        // Set callback
                        callback(account, null)
                    }

                } else {
                    callback(null, "Unsupported login request")
                }
            }

            AuthMethod.ANONYMOUS -> {
                if (loginRequest is AuthRequest.AnonymousLoginRequest) {
                    // TODO: 2/20/2020 Perform anonymous login 
                } else {
                    callback(null, "Unsupported login request")
                }
            }

            AuthMethod.FACEBOOK, AuthMethod.GOOGLE, AuthMethod.TWITTER -> {
                if (loginRequest is AuthRequest.FederatedLoginRequest) {
                    // TODO: 2/20/2020 Perform federated login 
                } else {
                    callback(null, "Unsupported login request")
                }
            }
        }
    }

}

/**
 * Callback for authentication
 */
typealias AuthCallback<R> = (result: R?, errorMessage: String?) -> Unit

/**
 * Supported authentication methods
 */
enum class AuthMethod {
    EMAIL_PASSWORD, GOOGLE, TWITTER, ANONYMOUS, FACEBOOK
}

/**
 * Types of supported login requests
 */
sealed class AuthRequest {
    data class LoginRequest(
        val email: String,
        val password: String,
        val isNewUser: Boolean = false  // default is false -> sign in only
    ) : AuthRequest()

    data class FederatedLoginRequest(val token: String) : AuthRequest()

    object AnonymousLoginRequest : AuthRequest()
}