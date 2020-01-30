package io.codelabs.todoapplication.room.repository

import io.codelabs.todoapplication.core.TodoApplication
import io.codelabs.todoapplication.data.TodoItem
import io.codelabs.todoapplication.room.TodoAppDao
import io.codelabs.todoapplication.room.TodoAppDatabase

class TodoTaskRepository constructor(application: TodoApplication) {
    private var dao: TodoAppDao = TodoAppDatabase.getInstance(application.applicationContext).dao()

    fun insert(todoItem: TodoItem) =  dao.createTodoItem(todoItem)

    fun delete(todoItem: TodoItem) =  dao.deleteItem(todoItem)

    fun getSingleItem(id: Int) = dao.getTodoItem(id)

    fun update(todoItem: TodoItem) = dao.updateTodoItem(todoItem)

    fun getItems() =  dao.getAllItems()

}