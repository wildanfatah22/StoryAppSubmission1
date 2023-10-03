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
import retrofit2.Call
import retrofit2.Response


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

    fun getResponseLogin(loginDataAccount: LoginAccount) {
        _isLoadingLogin.value = true
        val api = ApiConfig.getApiService().loginUser(loginDataAccount)
        api.enqueue(object : retrofit2.Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _isLoadingLogin.value = false
                val responseBody = response.body()

                if (response.isSuccessful) {
                    isErrorLogin = false
                    _userLogin.value = responseBody!!
                    _messageLogin.value = "Halo ${_userLogin.value!!.loginResult.name}!"
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
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                isErrorLogin = true
                _isLoadingLogin.value = false
                _messageLogin.value = "Pesan error: " + t.message.toString()
            }

        })
    }

    fun getResponseRegister(registDataUser: RegisterAccount) {
        _isLoadingRegister.value = true
        val api = ApiConfig.getApiService().registerUser(registDataUser)
        api.enqueue(object : retrofit2.Callback<DetailResponse> {
            override fun onResponse(
                call: Call<DetailResponse>,
                response: Response<DetailResponse>
            ) {
                _isLoadingRegister.value = false
                if (response.isSuccessful) {
                    isErrorRegister = false
                    _messageRegister.value = "Yeay akun berhasil dibuat"
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
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                isErrorRegister = true
                _isLoadingRegister.value = false
                _messageRegister.value = "Pesan error: " + t.message.toString()
            }

        })
    }

}