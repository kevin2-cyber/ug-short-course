package shortcourse.readium.core.database

import androidx.room.Dao
import shortcourse.readium.core.model.account.Account

@Dao
interface AccountDao : BaseDao<Account>