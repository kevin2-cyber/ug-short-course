package io.ugshortcourse.voteme.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * [ViewModelProvider.Factory] instance for [CandidateViewModel]
 */
class CandidateViewModelFactory constructor(private val repository: CandidateRepository, private val category: String) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return CandidateViewModel(repository, category) as T
    }
}