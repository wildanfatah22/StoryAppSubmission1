package com.example.storyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.data.local.entity.StoryDetailResponse
import com.example.storyapp.data.repository.UserRepository

class MainViewModel(private val userRepository: UserRepository) : ViewModel() {

    val stories: LiveData<List<StoryDetailResponse>> = userRepository.stories

    val message: LiveData<String> = userRepository.message

    val isLoading: LiveData<Boolean> = userRepository.isLoading

    @ExperimentalPagingApi
    fun getPagingStories(token: String): LiveData<PagingData<StoryDetailResponse>> {
        return userRepository.getPagingStories(token).cachedIn(viewModelScope)
    }

    fun getStories(token: String) {
        userRepository.getStories(token)
    }
}