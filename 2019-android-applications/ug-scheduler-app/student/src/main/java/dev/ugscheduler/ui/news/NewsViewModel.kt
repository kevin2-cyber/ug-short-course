/*
 * Copyright (c) 2019. Designed & developed by Quabynah Codelabs(c). For the love of Android development.
 */

package dev.ugscheduler.ui.news

import androidx.lifecycle.*
import dev.ugscheduler.shared.data.News
import dev.ugscheduler.shared.repository.AppRepository
import kotlinx.coroutines.launch

/**
 * Factory instance for news repository
 */
class NewsViewModelFactory(private val repository: AppRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewsViewModel(repository) as T
    }

}

/**
 * News view model instance
 */
class NewsViewModel(private val repository: AppRepository) : ViewModel() {
    private val _news = MutableLiveData<MutableList<News>>()

    init {
        viewModelScope.launch {
            _news.postValue(repository.getNewsUpdates(false))
        }
    }

    val news: LiveData<MutableList<News>> get() = _news
}
