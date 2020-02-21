package shortcourse.readium.core.database

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import shortcourse.readium.core.model.account.Account

@Dao
interface AccountDao : BaseDao<Account> {

    @Query("select * from accounts where id = :id")
    fun getAccount(id: String): Flow<Account>

    @Query("select * from accounts order by id desc")
    fun getAllAccounts(): Flow<MutableList<Account>>

    @Query("delete from accounts")
    suspend fun deleteAll()

    @Query("delete from accounts where id = :id")
    suspend fun delete(id: String)

}