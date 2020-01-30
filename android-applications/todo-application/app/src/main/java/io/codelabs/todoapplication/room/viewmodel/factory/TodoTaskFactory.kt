package io.codelabs.todoapplication.room.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.codelabs.todoapplication.core.TodoApplication
import io.codelabs.todoapplication.room.viewmodel.TodoTaskViewModel

class TodoTaskFactory constructor(private val application: TodoApplication) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return TodoTaskViewModel(application) as T
    }
}