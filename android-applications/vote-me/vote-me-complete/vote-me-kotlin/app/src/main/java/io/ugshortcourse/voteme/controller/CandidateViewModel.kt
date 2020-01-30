package io.ugshortcourse.voteme.controller

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.ugshortcourse.voteme.model.Candidate

/**
 * [ViewModel] for [Candidate]s
 */
class CandidateViewModel constructor() : ViewModel() {

    private var _candidates: LiveData<MutableList<Candidate>> = MutableLiveData()

    constructor(repository: CandidateRepository, category: String) : this() {
        _candidates = repository.getCandidates(category)
    }

    val candidates: LiveData<MutableList<Candidate>> = _candidates

}