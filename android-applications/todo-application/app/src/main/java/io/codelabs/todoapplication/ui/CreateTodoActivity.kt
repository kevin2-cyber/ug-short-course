package io.codelabs.todoapplication.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import io.codelabs.todoapplication.R
import io.codelabs.todoapplication.core.TodoApplication
import io.codelabs.todoapplication.data.TodoItem
import io.codelabs.todoapplication.room.viewmodel.TodoTaskViewModel
import io.codelabs.todoapplication.room.viewmodel.factory.TodoTaskFactory
import io.codelabs.todoapplication.util.createAlarm
import io.codelabs.todoapplication.util.debugLog
import io.codelabs.todoapplication.util.toast
import kotlinx.android.synthetic.main.activity_create_todo.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

/**
 * Create a new [TodoItem]
 */
class CreateTodoActivity : AppCompatActivity() {
    private val job = Job()
    private val ioScope = CoroutineScope(Dispatchers.IO + job)
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    private lateinit var viewModel: TodoTaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_todo)

        toolbar.setNavigationOnClickListener { finishAfterTransition() }
        viewModel = ViewModelProviders.of(this, TodoTaskFactory(application as TodoApplication))
            .get(TodoTaskViewModel::class.java)

        val intent = intent
        if (intent.hasExtra(EXTRA_ITEM)) {
            val todoItem = intent.getParcelableExtra<TodoItem>(EXTRA_ITEM)
            todo_input.setText(todoItem.content)
        }

    }

    fun saveTodoItem(v: View?) {
        // Get the text from the input box
        val content = todo_input.text.toString()

        // Check whether or not the text is empty
        if (content.isEmpty()) {
            this@CreateTodoActivity.toast(message = "Please enter a task todo")
        } else {
            ioScope.launch {
                if (intent.hasExtra(EXTRA_ITEM)) viewModel.update(intent.getParcelableExtra<TodoItem>(EXTRA_ITEM).apply {
                    this.content = content
                    this.timestamp = System.currentTimeMillis()
                    debugLog("Applying changes")
                })
                else viewModel.insert(TodoItem(content))

                // Update UI with callback
                uiScope.launch {
                    toast("Create todo item successfully")
                    finishAfterTransition()
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    companion object {
        const val EXTRA_ITEM = "EXTRA_ITEM"
    }
}