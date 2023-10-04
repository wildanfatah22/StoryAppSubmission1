package com.example.storyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.data.api.ApiConfig
import com.example.storyapp.data.response.DetailResponse
import com.example.storyapp.data.response.LoginAccount
import com.example.storyapp.data.response.LoginResponse
import com.example.storyapp.data.response.RegisterAccount
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch



class AuthViewModel : ViewModel() {

    private val _isLoadingLogin = MutableLiveData<Boolean>()
    val isLoadingLogin: LiveData<Boolean> = _isLoadingLogin
    var isErrorLogin: Boolean = false
    private val _messageLogin = MutableLiveData<String>()
    val messageLogin: LiveData<String> = _messageLogin
    private val _userLogin = MutableLiveData<LoginResponse>()
    val userLogin: LiveData<LoginResponse> = _userLogin

    var isErrorRegister: Boolean = false
    private val _isLoadingRegister = MutableLiveData<Boolean>()
    val isLoadingRegister: LiveData<Boolean> = _isLoadingRegister
    private val _messageRegister = MutableLiveData<String>()
    val messageRegister: LiveData<String> = _messageRegister

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun getResponseLogin(loginAccount: LoginAccount) {
        _isLoadingLogin.value = true
        coroutineScope.launch {
            try {
                val response = ApiConfig.getApiService().loginUser(loginAccount)
                _isLoadingLogin.value = false

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        isErrorLogin = false
                        _userLogin.value = responseBody!!
                        _messageLogin.value = "Halo ${_userLogin.value!!.loginResult.name}!"
                    } else {
                        isErrorLogin = true
                        _messageLogin.value = "Response body is null"
                    }
                } else {
                    isErrorLogin = true
                    when (response.code()) {
                        401 -> _messageLogin.value =
                            "Email atau password yang anda masukan salah, silahkan coba lagi"
                        408 -> _messageLogin.value =
                            "Koneksi internet anda lambat, silahkan coba lagi"
                        else -> _messageLogin.value = "Pesan error: " + response.message()
                    }
                }
            } catch (e: Exception) {
                isErrorLogin = true
                _isLoadingLogin.value = false
                _messageLogin.value = "Pesan error: " + e.message.toString()
            }
        }
    }

    fun getResponseRegister(registDataUser: RegisterAccount) {
        _isLoadingRegister.value = true
        coroutineScope.launch {
            try {
                val response = ApiConfig.getApiService().registerUser(registDataUser)
                _isLoadingRegister.value = false

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        isErrorRegister = false
                        _messageRegister.value = "Yeay akun berhasil dibuat"
                    } else {
                        isErrorRegister = true
                        _messageRegister.value = "Response body is null"
                    }
                } else {
                    isErrorRegister = true
                    when (response.code()) {
                        400 -> _messageRegister.value =
                            "1"
                        408 -> _messageRegister.value =
                            "Koneksi internet anda lambat, silahkan coba lagi"
                        else -> _messageRegister.value = "Pesan error: " + response.message()
                    }
                }
            } catch (e: Exception) {
                isErrorRegister = true
                _isLoadingRegister.value = false
                _messageRegister.value = "Pesan error: " + e.message.toString()
            }
        }
    }
}