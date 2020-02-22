package shortcourse.readium.core.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import shortcourse.readium.core.repository.AccountRepository
import shortcourse.readium.core.repository.AuthMethod
import shortcourse.readium.core.repository.AuthRequest
import shortcourse.readium.core.util.debugger

class AuthViewModel(
    private val repository: AccountRepository
) : ViewModel() {

    private val job = Job()
    private val authScope = viewModelScope + job

    enum class AuthenticationState {
        UNAUTHENTICATED, AUTHENTICATED, AUTHENTICATING, INVALID_AUTHENTICATION, CANCELLED
    }

    private val _authState =
        MutableLiveData<AuthenticationState>(AuthenticationState.UNAUTHENTICATED)
    val authState: LiveData<AuthenticationState> get() = _authState


    /**
     * Cancel any ongoing authentication
     */
    fun cancelAuthentication() {
        _authState.value = AuthenticationState.CANCELLED
        job.cancel()
    }

    /**
     * Authenticate user based on credentials
     */
    fun authenticateAccount(email: String, password: String) {
        _authState.value = AuthenticationState.AUTHENTICATING

        authScope.launch {
            repository.authenticate(
                AuthMethod.EMAIL_PASSWORD,
                AuthRequest.LoginRequest(email, password)
            ) { result, errorMessage ->
                if (!errorMessage.isNullOrEmpty() || result == null) {
                    debugger(errorMessage)
                    // Login failed
                    _authState.value = AuthenticationState.INVALID_AUTHENTICATION
                    return@authenticate
                }

                // Login was successful
                _authState.value = AuthenticationState.AUTHENTICATED
            }
        }
    }

    fun createAccount(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        penName: String = ""
    ) {
        _authState.value = AuthenticationState.AUTHENTICATING

        // TODO: 2/20/2020 Perform some action with new user data

        authScope.launch {
            repository.authenticate(
                AuthMethod.EMAIL_PASSWORD,
                AuthRequest.LoginRequest(email, password, true)
            ) { result, errorMessage ->
                if (!errorMessage.isNullOrEmpty() || result == null) {
                    debugger(errorMessage)
                    // Login failed
                    _authState.value = AuthenticationState.INVALID_AUTHENTICATION
                    return@authenticate
                }

                // Login was successful
                _authState.value = AuthenticationState.AUTHENTICATED
            }
        }
    }

    fun logout() {
        authScope.launch {
            repository.logout()
            _authState.value = AuthenticationState.UNAUTHENTICATED
        }
    }

}