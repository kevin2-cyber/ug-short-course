package shortcourse.readium.core.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import shortcourse.readium.core.model.post.Comment
import shortcourse.readium.core.repository.CommentRepository

class CommentViewModel(private val repository: CommentRepository) : ViewModel() {
    private val _comments = MutableLiveData<MutableList<Comment>>()
    private val _singleComment = MutableLiveData<Comment>()

    val singleComment: LiveData<Comment> get() = _singleComment
    val comments: LiveData<MutableList<Comment>> get() = _comments

    fun getCommentsForPost(postId: String) = viewModelScope.launch {
        repository.getAllComments(postId).collect {
            _comments.postValue(it.dataOrNull())
        }
    }


    fun getComment(id: String) = viewModelScope.launch {
        repository.getSingleComment(id).collect {
            _singleComment.postValue(it.dataOrNull())
        }
    }

}