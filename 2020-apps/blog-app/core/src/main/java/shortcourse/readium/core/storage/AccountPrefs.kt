package shortcourse.readium.core.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import shortcourse.readium.core.model.account.Account
import shortcourse.readium.core.util.Entities
import shortcourse.readium.core.util.StorageUtil

/**
 * Maintains the user's login state
 */
class AccountPrefs private constructor(context: Context) {
    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(
            StorageUtil.ACCOUNT_PREFS,
            Context.MODE_PRIVATE
        )
    }

    /**
     * Returns the current
     */
    var authToken: String? = null
        private set(value) {
            prefs.edit {
                putString(StorageUtil.Keys.ID, value)
                apply()
            }
            _liveState.map {
                return@map !value.isNullOrEmpty()
            }
            field = value
        }
        get() = prefs.getString(StorageUtil.Keys.ID, null)

    var roles: MutableList<String> = mutableListOf(Entities.Role.GUEST.label)
        private set(value) {
            prefs.edit {
                putString(StorageUtil.Keys.ROLES, Gson().toJson(value))
                apply()
            }
            field = value
        }
        get() = generateRoleDefaults()

    // Gets the current login state
    val isLoggedIn: Boolean get() = !authToken.isNullOrEmpty()

    private val _liveState: Flow<Boolean> = flow {
        emit(isLoggedIn)
    }

    val currentState: Flow<Boolean> get() = _liveState

    init {
        // Initial values
        authToken = prefs.getString(StorageUtil.Keys.ID, null)
        roles = generateRoleDefaults()

        // Update flow state
        _liveState.map {
            return@map !authToken.isNullOrEmpty()
        }
    }

    /**
     * Login user
     */
    fun login(account: Account) {
        roles = account.roles
        authToken = account.id
    }

    /**
     * Logout user
     */
    fun logout() {
        prefs.edit {
            clear()
            apply()
        }
        _liveState.map {
            return@map false
        }
    }

    // Generates the default values for the roles
    private fun generateRoleDefaults(): MutableList<String> = Gson().fromJson<List<String>>(
        prefs.getString(
            StorageUtil.Keys.ROLES,
            Gson().toJson(mutableListOf(Entities.Role.GUEST.label))
        ),
        object : TypeToken<List<String>>() {}.type
    ).toMutableList()

    companion object {
        @Volatile
        private var instance: AccountPrefs? = null

        fun getInstance(context: Context): AccountPrefs = instance ?: synchronized(this) {
            instance ?: AccountPrefs(context).also { instance = it }
        }
    }

}