package shortcourse.legere

import org.junit.Test
import shortcourse.legere.model.entities.BaseEntity
import shortcourse.legere.model.entities.Roles
import shortcourse.legere.model.entities.User
import java.sql.Date as SqlDate
import java.util.Date as JavaDate


/**
 * Running unit tests
 */
class ReadiumUnitTest {

    // Checks the current timestamp
    @Test
    fun checkTimestamp() {
        val currentTimeMillis = System.currentTimeMillis()
        println("Current timestamp -> $currentTimeMillis")
        println("Java Date from timestamp -> ${JavaDate(currentTimeMillis)}")
        println("SQL Date from timestamp -> ${SqlDate(currentTimeMillis)}")
    }

    // Checks for inheritance of the User class
    @Test
    fun checkEntity() {
        val user = User(id = "1234", email = "q@g.co")  // Named parameters
        val secondUser =
            User("1234", "e@f.com")
        secondUser.avatar = "dp"
        // sendBackToServer(secondUser)
        assert(user is BaseEntity)
    }
}