package shortcourse.readium.core.database

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import shortcourse.readium.core.model.account.Account

@Dao
interface AccountDao : BaseDao<Account> {

    @Query("select * from accounts where id = :id")
    fun getAccount(id: String): Flow<Account>

}