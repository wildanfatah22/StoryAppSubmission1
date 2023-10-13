package com.example.storyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.data.repository.UserRepository
import com.example.storyapp.data.response.LoginAccount
import com.example.storyapp.data.response.LoginResponse
import com.example.storyapp.data.response.RegisterAccount


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