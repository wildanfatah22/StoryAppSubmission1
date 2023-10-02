package com.example.storyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.datastore.UserPreferences
import com.example.storyapp.data.repository.MyRepository
import com.example.storyapp.data.repository.UserRepository
import com.example.storyapp.data.response.RegisterAccount
import kotlinx.coroutines.launch

class RegisterViewModel(private val myRepository: MyRepository, private val userPreferences: UserPreferences) : ViewModel() {

    /** 1
    suspend fun register(registerAccount: RegisterAccount) {
        userRepository.getResponseRegister(registerAccount)
    }

    suspend fun saveLoginSessionAndToken(token: String, name: String) {
        // Simpan token dan sesi login ke preferences
        userPreferences.saveToken(token)
        userPreferences.saveName(name)
        userPreferences.saveLoginSession(true)
    }
    **/

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun userRegister(registerAccount: RegisterAccount) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = myRepository.userRegister(registerAccount)
                if (!response.error) {
                    _message.value = response.message
                } else {
                    _message.value = "Failed to register user"
                }
            } catch (e: Exception) {
                _message.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

}