package io.codelabs.todoapplication.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import io.codelabs.todoapplication.R
import io.codelabs.todoapplication.core.TodoApplication
import io.codelabs.todoapplication.data.TodoItem
import io.codelabs.todoapplication.room.viewmodel.TodoTaskViewModel
import io.codelabs.todoapplication.room.viewmodel.factory.TodoTaskFactory
import io.codelabs.todoapplication.ui.adapter.TodoTaskAdapter
import io.codelabs.todoapplication.util.toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), TodoTaskAdapter.ClickListener {
    private lateinit var viewModel: TodoTaskViewModel
    private val job = Job()
    private val ioScope = CoroutineScope(Dispatchers.IO + job)
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar.title = getString(R.string.empty_text)

        val layoutManager = LinearLayoutManager(this)
        grid_todos.layoutManager = layoutManager
        grid_todos.setHasFixedSize(true)
        grid_todos.itemAnimator = DefaultItemAnimator()
        val adapter = TodoTaskAdapter(this)
        grid_todos.adapter = adapter

        // Create view model instance
        viewModel = ViewModelProviders.of(this, TodoTaskFactory(application as TodoApplication))
            .get(TodoTaskViewModel::class.java)

        uiScope.launch {
            viewModel.getTodoItems().observe(this@MainActivity,
                Observer<MutableList<TodoItem>?> { items ->
                    if (items != null) {
                        adapter.addItems(items)
                    }
                })
        }
    }

    fun addNewTodo(v: View?) = startActivity(Intent(this, CreateTodoActivity::class.java))

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_search -> {
                //todo: search implementation
                toast(message = "Not available")
            }

            R.id.menu_filter -> {
                //todo: search implementation
                toast(message = "Not available")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onClick(item: TodoItem) {
        if (item.completed) {
            val make = Snackbar.make(container, "Clear this item completely?", Snackbar.LENGTH_LONG)

            make.setAction("Delete") {
                make.dismiss()
                ioScope.launch {
                    viewModel.delete(item)

                    uiScope.launch {
                        make.setText("Item deleted successfully")
                        make.setDuration(BaseTransientBottomBar.LENGTH_SHORT).show()
                    }
                }
            }.show()
        } else {
            // Create intent object
            val intent = Intent(this@MainActivity, DetailsActivity::class.java)

            // Add data to intent
            intent.putExtra(/*key*/DetailsActivity.EXTRA_ITEM,/*value*/item)

            // Start activity with intent object
            startActivity(intent)
        }
    }
}
