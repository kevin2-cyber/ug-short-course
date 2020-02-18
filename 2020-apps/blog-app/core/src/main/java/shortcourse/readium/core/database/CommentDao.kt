package shortcourse.readium.core.database

import androidx.room.Dao
import shortcourse.readium.core.model.post.Comment

@Dao
interface CommentDao : BaseDao<Comment>