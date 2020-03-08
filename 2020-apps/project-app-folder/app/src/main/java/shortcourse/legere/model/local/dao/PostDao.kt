package shortcourse.legere.model.local.dao

import androidx.room.Dao
import androidx.room.Query
import shortcourse.legere.model.entities.Post

/**
 * Defines all transaction that can be made on the [Post] entity
 */
@Dao
interface PostDao : ReadiumDao<Post> {

    // Get all posts in the database
    @Query("select * from posts order by id desc")
    fun getAllPosts(): MutableList<Post>

    // Gets a single post by its ID
    @Query("select * from posts where id = :id")
    fun getPostById(id: String): Post?

    // Gets all posts uploaded by an author with id  -> AuthorId
    @Query("select * from posts where authorId = :authorId")
    fun getPostByAuthor(authorId: String): MutableList<Post>

}