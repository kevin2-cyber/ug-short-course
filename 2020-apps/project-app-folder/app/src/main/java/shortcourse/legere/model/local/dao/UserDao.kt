package shortcourse.legere.model.local.dao

import androidx.room.Dao
import androidx.room.Query
import shortcourse.legere.model.entities.User

/**
 * Defines all transaction that can be made on the [User] entity
 */
@Dao
interface UserDao : ReadiumDao<User> {

    // Gets a user by id
    @Query("select * from users where id = :id")
    fun getUserById(id: String): User?

}