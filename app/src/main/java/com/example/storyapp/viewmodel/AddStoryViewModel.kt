package com.example.storyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.data.repository.UserRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(private val userRepository: UserRepository) : ViewModel() {
    val message: LiveData<String> =userRepository.message

    val isLoading: LiveData<Boolean> = userRepository.isLoading


    fun upload(
        photo: MultipartBody.Part,
        des: RequestBody,
        lat: Double?,
        lng: Double?,
        token: String
    ) {
        userRepository.upload(photo, des, lat, lng, token)
    }
}