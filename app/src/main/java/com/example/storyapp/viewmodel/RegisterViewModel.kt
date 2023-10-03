package com.example.storyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.datastore.UserPreferences
import com.example.storyapp.data.response.RegisterAccount
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

//class RegisterViewModel(private val myRepository: MyRepository) : ViewModel() {
//
//    private val _isLoadingRegist = MutableLiveData<Boolean>()
//    val isLoadingRegist: LiveData<Boolean> = _isLoadingRegist
//
//    var isErrorRegist: Boolean = false
//
//    private val _messageRegist = MutableLiveData<String>()
//    val messageRegist: LiveData<String> = _messageRegist
//
//    fun getResponseRegister(registerAccount: RegisterAccount) {
//        _isLoadingRegist.value = true
//        viewModelScope.launch {
//            val response = myRepository.userRegister(registerAccount)
//            _isLoadingRegist.value = false
//            _messageRegist.value = response.message
//        }
//    }
//}