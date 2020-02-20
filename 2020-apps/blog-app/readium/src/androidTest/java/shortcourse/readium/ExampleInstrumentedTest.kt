package shortcourse.readium

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import shortcourse.readium.core.model.account.Account
import shortcourse.readium.core.storage.AccountPrefs
import shortcourse.readium.core.util.Entities
import shortcourse.readium.core.util.debugger
import java.util.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    private val t = InstrumentationRegistry.getInstrumentation()

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = t.targetContext
        assertEquals("shortcourse.readium", appContext.packageName)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun useAccountPrefs() {
        val targetContext = t.context
        val prefs = AccountPrefs.getInstance(targetContext)
        with(prefs) {
            GlobalScope.launch(Dispatchers.IO) {
                currentState.collect {
                    debugger("Current state -> $it")
                    debugger("Auth token -> $authToken")
                    debugger("Roles -> $roles")
                }
            }

            val acct = Account(
                UUID.randomUUID().toString(),
                "Samuel",
                "Bilson",
                "sammy@gmail.com",
                "Sammy Blinks",
                roles = mutableListOf(Entities.Role.AUTHOR.label, Entities.Role.EDITOR.label)
            )
            login(acct)
        }
    }
}