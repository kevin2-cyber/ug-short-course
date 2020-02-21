package shortcourse.readium.core

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ReadiumUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
        for (i in 0 until 12) {
            println(UUID.randomUUID().toString())
            println(System.currentTimeMillis())
        }
    }
}