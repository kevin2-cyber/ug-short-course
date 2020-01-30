package io.codelabs.todoapplication.room.viewmodel

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import io.codelabs.todoapplication.core.TodoApplication
import io.codelabs.todoapplication.data.TodoItem
import io.codelabs.todoapplication.room.repository.TodoTaskRepository

class TodoTaskViewModel constructor(application: TodoApplication) : AndroidViewModel(application) {
    private val repository = TodoTaskRepository(application)

    fun getTodoItems(): LiveData<MutableList<TodoItem>> = repository.getItems()

    fun insert(todoItem: TodoItem) = repository.insert(todoItem)

    fun update(todoItem: TodoItem) = repository.update(todoItem)

    fun delete(todoItem: TodoItem) = repository.delete(todoItem)

    fun getTodoItem(id: Int) = repository.getSingleItem(id)
}