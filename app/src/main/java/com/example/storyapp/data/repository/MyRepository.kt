package com.example.storyapp.data.repository

import com.example.storyapp.data.api.ApiConfig
import com.example.storyapp.data.api.ApiService
import com.example.storyapp.data.response.LoginAccount
import com.example.storyapp.data.response.RegisterAccount
import retrofit2.Response
import java.lang.Exception

/**
class MyRepository(private val apiService: ApiService) {

    suspend fun loginUser(loginAccount: LoginAccount): Response<ResponseLogin> {
        return  ApiConfig.getApiService().userLogin(loginAccount)
    }

    suspend fun userRegister(registerAccount: RegisterAccount): ResponseDetail {
        return try {
            val response = ApiConfig.getApiService().userRegister(registerAccount)
            if (response.error) {
                ResponseDetail(true, "Failed to register user")
            } else {
                ResponseDetail(false, "Account created!")
            }
        } catch (e: Exception) {
            ResponseDetail(true, "Error message: ${e.message}")
        }
    }

//    suspend fun userLogin(loginAccount: LoginAccount): ResponseLogin {
//        return apiService.userLogin(loginAccount)
//    }

    suspend fun userLogin(loginAccount: LoginAccount): ResponseLogin {
        return try {
            val response = ApiConfig.getApiService().userLogin(loginAccount)
            if (response.error) {
                ResponseLogin(true, "Failed to login")
            } else {
                ResponseLogin(false, "Login success!")
            }
        } catch (e: Exception) {
            ResponseLogin(true, "Error message: ${e.message}")
        }
    }
}**/