package com.example.storyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.data.api.ApiConfig
import com.example.storyapp.data.repository.UserRepository
import com.example.storyapp.data.response.DetailResponse
import com.example.storyapp.data.response.LoginAccount
import com.example.storyapp.data.response.LoginResponse
import com.example.storyapp.data.response.RegisterAccount
import retrofit2.Call
import retrofit2.Response


class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {

    val message: LiveData<String> = userRepository.message

    val isLoading: LiveData<Boolean> = userRepository.isLoading

    val userLogin: LiveData<LoginResponse> = userRepository.userLogin


    fun login(loginAccount: LoginAccount) {
        userRepository.getLoginResponse(loginAccount)
    }

    fun register(registerAccount: RegisterAccount) {
        userRepository.getResponseRegister(registerAccount)
    }
}