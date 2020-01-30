package io.codelabs.todoapplication.module

import io.codelabs.todoapplication.room.TodoAppDatabase
import io.codelabs.todoapplication.room.repository.TodoTaskRepository
import io.codelabs.todoapplication.room.viewmodel.TodoTaskViewModel
import io.codelabs.todoapplication.room.viewmodel.factory.TodoTaskFactory
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val roomModule = module {
    single { TodoAppDatabase.getInstance(get()) }
}

val appModule = module {
    single { TodoTaskFactory(get()) }
    single { TodoTaskRepository(get()) }
    viewModel { TodoTaskViewModel(get()) }
}