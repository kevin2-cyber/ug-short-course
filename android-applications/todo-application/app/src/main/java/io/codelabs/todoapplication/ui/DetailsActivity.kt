package io.codelabs.todoapplication.ui

import android.content.Intent
import android.os.Bundle
import android.text.format.DateUtils
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import io.codelabs.todoapplication.R
import io.codelabs.todoapplication.core.TodoApplication
import io.codelabs.todoapplication.data.TodoItem
import io.codelabs.todoapplication.room.viewmodel.TodoTaskViewModel
import io.codelabs.todoapplication.room.viewmodel.factory.TodoTaskFactory
import io.codelabs.todoapplication.util.debugLog
import io.codelabs.todoapplication.util.toast
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DetailsActivity : AppCompatActivity() {
    private val job = Job()
    private val ioScope = CoroutineScope(Dispatchers.IO + job)
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    private lateinit var viewModel: TodoTaskViewModel
    private lateinit var todoItem: TodoItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        // Toolbar setup
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { finishAfterTransition() }

        // Init view model
        viewModel = ViewModelProviders.of(this, TodoTaskFactory(application as TodoApplication))
            .get(TodoTaskViewModel::class.java)

        val intent = intent
        if (intent.hasExtra(EXTRA_ITEM)) {
            todoItem = intent.getParcelableExtra(EXTRA_ITEM)
            bindItem()

            ioScope.launch {
                val liveData = viewModel.getTodoItem(todoItem.id)
                uiScope.launch {
                    liveData.observe(this@DetailsActivity,
                        Observer<TodoItem?> {
                            if (it != null) {
                                todoItem = it
                                debugLog("Binding from observer. $it")
                                bindItem()
                            }
                        })
                }
            }
        }
    }

    private fun bindItem() {
        todo_detail_title.text = todoItem.content
        todo_detail_timestamp.text = DateUtils.getRelativeTimeSpanString(
            todoItem.timestamp,
            System.currentTimeMillis(),
            DateUtils.SECOND_IN_MILLIS
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.todo_item_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_delete -> {
                ioScope.launch {
                    viewModel.delete(todoItem)
                    uiScope.launch {
                        this@DetailsActivity.toast(message = "Todo item deleted successfully")
                        finishAfterTransition()
                    }
                }
            }

            R.id.menu_edit -> {
                val intent = Intent(this@DetailsActivity, CreateTodoActivity::class.java)
                intent.putExtra(CreateTodoActivity.EXTRA_ITEM, todoItem)
                startActivity(intent)
            }

            R.id.menu_share -> {
                // Share content across devices
                val shareIntent = ShareCompat.IntentBuilder.from(this@DetailsActivity)
                    .setType("text/plain")
                    .setText(todoItem.content)
                    .intent
                if (shareIntent.resolveActivity(packageManager) != null) {
                    startActivity(shareIntent)
                } else toast(message = "No application can handle this action")
            }

            R.id.menu_completed -> {
                // Update the property of the todoItem
                todoItem.completed = true

                // Store the item in the database
                ioScope.launch {
                    viewModel.update(todoItem)

                    uiScope.launch {
                        this@DetailsActivity.toast(message = "Updated successfully")
                        finishAfterTransition()
                    }
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    companion object {
        const val EXTRA_ITEM = "EXTRA_ITEM"
    }
}