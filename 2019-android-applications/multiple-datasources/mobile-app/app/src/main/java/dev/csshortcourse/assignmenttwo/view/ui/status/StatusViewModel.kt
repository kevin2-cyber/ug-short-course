package dev.csshortcourse.assignmenttwo.view.ui.status

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StatusViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Status Fragment"
    }
    val text: LiveData<String> = _text
}