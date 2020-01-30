package dev.csshortcourse.assignmenttwo.view.ui.groups

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GroupViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Groups Fragment"
    }
    val text: LiveData<String> = _text
}