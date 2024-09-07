package com.dicoding.storyapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.storyapp.data.UserRepository
import com.dicoding.storyapp.data.pref.UserModel
import com.dicoding.storyapp.data.response.ListStoryItem
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class MainViewModel(private val repository: UserRepository) : ViewModel() {

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    val story: LiveData<PagingData<ListStoryItem>> = repository.getStory().cachedIn(viewModelScope)
}