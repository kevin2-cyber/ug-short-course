package io.codelabs.todoapplication.room

import androidx.lifecycle.LiveData
import androidx.room.*
import io.codelabs.todoapplication.data.TodoItem

/**
 * Data Access Object
 */
@Dao
interface TodoAppDao {

    /**
     * Create a new [TodoItem]
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createTodoItem(vararg todoItem: TodoItem)

    /**
     * Get a list of all [TodoItem]s
     */
    @Query("SELECT * FROM todos ORDER BY timestamp DESC")
    fun getAllItems(): LiveData<MutableList<TodoItem>>

    /**
     * Get a single [TodoItem] by [id]
     */
    @Query("SELECT * FROM todos WHERE id = :id")
    fun getTodoItem(id: Int): LiveData<TodoItem>

    /**
     * Get a single [TodoItem] by content
     * Example:
     * class SearchActivity : AppCompatActivity() {
     *      private val viewmodel: SomeViewModel by lazy { SomeViewModel() }
     *
     *
     *      override fun onCreate(bundle: Bundle?) {
     *
     *          viewmodel.getTodoItem("I like washing").observe(this, Observer ({
     *              // Do something with the live Data result obtained from
     *              // the query
     *          })
     *      }
     * }
     *
     */
    @Query("SELECT * FROM todos WHERE content LIKE :content")
    fun getTodoItem(content: String): LiveData<TodoItem>

    /**
     * Delete a single [TodoItem]
     */
    @Delete
    fun deleteItem(vararg todoItem: TodoItem)

    /**
     * Update [todoItem]
     */
    @Update
    fun updateTodoItem(vararg todoItem: TodoItem)

}