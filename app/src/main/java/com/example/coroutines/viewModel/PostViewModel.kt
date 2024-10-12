package com.example.coroutines.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coroutines.data.model.Post
import com.example.coroutines.data.remote.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PostViewModel : ViewModel() {

    val postList = MutableLiveData<List<Post>>()
    val postListError = MutableLiveData<String?>()
    val loading = MutableLiveData<Boolean>()

    fun getAllPostsRequest() {

        loading.value = true

        viewModelScope.launch(Dispatchers.IO) {

        }

        CoroutineScope(Dispatchers.IO).launch {
            val response = try {
                ApiClient.api.getAllPosts()
            } catch (e: Exception) {
                postListError.value = e.toString()
                loading.value = false
                return@launch
            }
            withContext(Dispatchers.Main) {
                if (response.isSuccessful && response.body() != null) {
                    response.body()?.let { allPost ->
                        postList.value = allPost
                        postListError.value = null
                        loading.value = false
                    }
                } else {
                    postListError.value = response.message()
                    loading.value = false
                }
            }
        }


    }
}