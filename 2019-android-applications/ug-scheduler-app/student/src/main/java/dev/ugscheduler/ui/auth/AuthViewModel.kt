/*
 * Copyright (c) 2019. Designed & developed by Quabynah Codelabs(c). For the love of Android development.
 */

package dev.ugscheduler.ui.auth

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.*
import dev.ugscheduler.shared.data.Student
import dev.ugscheduler.shared.datasource.local.LocalDatabase
import dev.ugscheduler.shared.repository.AppRepository
import dev.ugscheduler.shared.util.BaseActivity
import dev.ugscheduler.shared.util.debugger
import dev.ugscheduler.shared.util.prefs.UserSharedPreferences
import dev.ugscheduler.shared.viewmodel.AppViewModel
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AuthViewModelFactory(private val repository: AppRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AuthViewModel(repository) as T
    }

}

class AuthViewModel(repository: AppRepository) : AppViewModel(repository) {
    private val _userInfo = MutableLiveData<Student?>()

    init {
        viewModelScope.launch {
            withContext(Main) {
                getCurrentStudent(false).observe(BaseActivity(), Observer { student ->
                    debugger("Observing student data: $student")
                    _userInfo.value = student
                })
            }
        }
    }

    val currentUserInfo: LiveData<Student?> get() = _userInfo

    fun onProfileClicked(fm: FragmentManager, loggedIn: Boolean) =
        if (loggedIn) SignOutFragment().show(fm, null) else SignInFragment().show(fm, null)
}