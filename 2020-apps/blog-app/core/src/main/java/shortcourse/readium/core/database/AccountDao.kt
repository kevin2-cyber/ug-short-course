package shortcourse.readium.core.database

import androidx.lifecycle.LiveData
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

}