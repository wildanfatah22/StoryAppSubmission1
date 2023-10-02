package com.example.storyapp.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.repository.MyRepository
import com.example.storyapp.data.response.LoginAccount

import kotlinx.coroutines.launch


class LoginViewModel(private val myRepository: MyRepository): ViewModel() {
    private val _isLoadingLogin = MutableLiveData<Boolean>()
    val isLoadingLogin: LiveData<Boolean> = _isLoadingLogin

    var isErrorLogin: Boolean = false

    private val _messageLogin = MutableLiveData<String>()
    val messageLogin: LiveData<String> = _messageLogin

    fun getResponseLogin(registerAccount: LoginAccount) {
        _isLoadingLogin.value = true
        viewModelScope.launch {
            val response = myRepository.userLogin(registerAccount)
            _isLoadingLogin.value = false
            _messageLogin.value = response.message
        }
    }


}