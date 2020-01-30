package io.codelabs.todoapplication.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.codelabs.todoapplication.R
import io.codelabs.todoapplication.data.TodoItem
import kotlinx.android.synthetic.main.item_todo.view.*

class TodoTaskAdapter constructor(private val listener: ClickListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    /**
     * Empty list of [TodoItem]s
     */
    private val dataset: MutableList<TodoItem> = mutableListOf()

    override fun getItemViewType(position: Int): Int = when {
        dataset.isEmpty() -> TYPE_EMPTY
        dataset[position].completed -> TYPE_TODO_COMPLETED
        else -> TYPE_TODO
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_EMPTY -> EmptyViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_empty, parent, false)
            )

            TYPE_TODO -> TodoViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_todo, parent, false)
            )

            else /*TYPE_TODO_COMPLETED*/ -> TodoViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_todo_completed, parent, false)
            )
        }
    }

    override fun getItemCount(): Int = if (dataset.isEmpty()) 1 else dataset.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {

            TYPE_TODO -> {
                if (holder is TodoViewHolder) {
                    // Get the todoItem for each position
                    val todoItem = dataset[position]

                    // Bind the content to the layout's title
                    holder.v.todo_item_title.text = todoItem.content

                    // Bind the timestamp to the timestamp field in the layout file
                    val timestamp: String = DateUtils.getRelativeTimeString(
                        todoItem.timestamp,
                        System.currentTimeMillis(),
                        DateUtils.SECONDS
                    )

                    // Set the timestamp to the result obtained
                    holder.v.todo_item_timestamp.text = timestamp

                    holder.v.setOnClickListener { listener.onClick(todoItem) }

                }
            }

            TYPE_TODO_COMPLETED -> {
                if (holder is TodoViewHolder) {
                    // Get the todoItem for each position
                    val todoItem = dataset[position]

                    // Bind the content to the layout's title
                    holder.v.completed_todo_item_title.text = todoItem.content

                    // Bind the timestamp to the timestamp field in the layout file
                    val timestamp: String = DateUtils.getRelativeTimeString(
                        todoItem.timestamp,
                        System.currentTimeMillis(),
                        DateUtils.SECONDS
                    )

                    // Set the timestamp to the result obtained
                    holder.v.completed_todo_item_timestamp.text = timestamp

                    holder.v.setOnClickListener { listener.onClick(todoItem) }
                }
            }

        }
    }

    // Add new items to the existing list of todoItems
    fun addItems(newItems: MutableList<TodoItem>) {
        dataset.clear() /*Empty the existing list*/
        dataset.addAll(newItems)    /*Populate new items into it*/
        notifyDataSetChanged()
    }

   interface ClickListener {
       fun onClick(item: TodoItem)
   }

    companion object {
        private const val TYPE_EMPTY = 0
        private const val TYPE_TODO = 1
        private const val TYPE_TODO_COMPLETED = 2
    }
}