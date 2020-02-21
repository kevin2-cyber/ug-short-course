package shortcourse.readium.core.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import shortcourse.readium.core.model.post.Post
import shortcourse.readium.core.repository.PostRepository

class PostViewModel(private val repository: PostRepository) : ViewModel() {

    private val _allPosts = MutableLiveData<MutableList<Post>>()
    val allPosts: LiveData<MutableList<Post>> get() = _allPosts

    fun getPostForAuthor(authorId: String) = viewModelScope.launch {
        repository.getPostsForAuthor(authorId).collect {
            _allPosts.value = it.dataOrNull()
        }
    }

}