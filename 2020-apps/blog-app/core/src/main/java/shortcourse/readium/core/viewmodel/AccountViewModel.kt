package shortcourse.readium.core.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import shortcourse.readium.core.model.account.Account
import shortcourse.readium.core.repository.AccountRepository

class AccountViewModel(private val accountRepository: AccountRepository) : ViewModel() {

    private val _currentUser = MutableLiveData<Account>()
    val currentUser: LiveData<Account> get() = _currentUser

    init {
        viewModelScope.launch {
            accountRepository.getCurrentUser().collect {
                val account = it.dataOrNull()
                _currentUser.postValue(account)
            }
        }
    }

}