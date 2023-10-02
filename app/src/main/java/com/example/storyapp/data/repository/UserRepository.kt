package com.example.storyapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.storyapp.data.api.ApiConfig
import com.example.storyapp.data.api.ApiService
import com.example.storyapp.data.response.LoginAccount
import com.example.storyapp.data.response.RegisterAccount
import com.example.storyapp.data.response.ResponseDetail
import com.example.storyapp.data.response.ResponseLogin
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UserRepository(private val apiService: ApiService) {

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _userLogin = MutableLiveData<ResponseLogin>()
    var userlogin: LiveData<ResponseLogin> = _userLogin

//    suspend fun getResponseLogin(loginAccount: LoginAccount) {
//        _isLoading.value = true
//        val api = ApiConfig.getApiService().userLogin(loginAccount)
//        api.enqueue(object : Callback<ResponseLogin> {
//            override fun onResponse(
//                call: Call<ResponseLogin>,
//                response: Response<ResponseLogin>
//            ) {
//                _isLoading.value = false
//                val responseBody = response.body()
//
//                if (response.isSuccessful) {
//                    _userLogin.value = responseBody!!
//                    _message.value = "Hello ${_userLogin.value!!.loginResult.name}!"
//                } else {
//                    when (response.code()) {
//                        401 -> _message.value =
//                            "The email or password you entered is incorrect, please try again"
//                        408 -> _message.value =
//                            "Your internet connection is slow, please try again"
//                        else -> _message.value = "Error message: " + response.message()
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
//                _isLoading.value = false
//                _message.value = "Error message: " + t.message.toString()
//            }
//        })
//    }
//
//    suspend fun getResponseRegister(registerAccount: RegisterAccount) {
//        _isLoading.value = true
//        val api = ApiConfig.getApiService().userRegister(registerAccount)
//        api.enqueue(object : Callback<ResponseDetail> {
//            override fun onResponse(
//                call: Call<ResponseDetail>,
//                response: Response<ResponseDetail>
//            ) {
//                _isLoading.value = false
//                if (response.isSuccessful) {
//                    _message.value = "The account has been successfully created!"
//                } else {
//                    when (response.code()) {
//                        400 -> _message.value =
//                            "The email you entered is already registered, please try again"
//                        408 -> _message.value =
//                            "Your internet connection is slow, please try again"
//                        else -> _message.value = "Error message: " + response.message()
//                    }
//                }
//            }
//            override fun onFailure(call: Call<ResponseDetail>, t: Throwable) {
//                _isLoading.value = false
//                _message.value = "Error message: " + t.message.toString()
//            }
//
//        })
//    }


}