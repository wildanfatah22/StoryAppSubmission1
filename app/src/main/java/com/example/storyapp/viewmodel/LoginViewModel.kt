package com.example.storyapp.viewmodel

//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.storyapp.data.repository.MyRepository
//import com.example.storyapp.data.response.LoginAccount
//
//import kotlinx.coroutines.launch
//
//
//class LoginViewModel(private val myRepository: MyRepository): ViewModel() {
//    private val _isLoadingLogin = MutableLiveData<Boolean>()
//    val isLoadingLogin: LiveData<Boolean> = _isLoadingLogin
//
//    var isErrorLogin: Boolean = false
//
//    private val _messageLogin = MutableLiveData<String>()
//    val messageLogin: LiveData<String> = _messageLogin
//
//    val loginResult: MutableLiveData<BaseResponse<LoginResponse>> = MutableLiveData()
//
//    fun loginUser(email: String, pwd: String) {
//
//        loginResult.value = BaseResponse.Loading()
//        viewModelScope.launch {
//            try {
//
//                val loginRequest = LoginRequest(
//                    password = pwd,
//                    email = email
//                )
//                val response = userRepo.loginUser(loginRequest = loginRequest)
//                if (response?.code() == 200) {
//                    loginResult.value = BaseResponse.Success(response.body())
//                } else {
//                    loginResult.value = BaseResponse.Error(response?.message())
//                }
//
//            } catch (ex: Exception) {
//                loginResult.value = BaseResponse.Error(ex.message)
//            }
//        }
//    }
//
//
//}