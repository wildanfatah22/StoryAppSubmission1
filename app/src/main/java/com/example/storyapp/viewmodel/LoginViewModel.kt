package com.example.storyapp.viewmodel


import androidx.lifecycle.ViewModel
import com.example.storyapp.data.repository.UserRepository
import com.example.storyapp.data.response.LoginAccount


class LoginViewModel(private val userRepository: UserRepository): ViewModel() {

    suspend fun login(loginAccount: LoginAccount) {
        userRepository.getResponseLogin(loginAccount)
    }


}