package com.example.storyapp.data.repository

import com.example.storyapp.data.api.ApiConfig
import com.example.storyapp.data.api.ApiService
import com.example.storyapp.data.response.LoginAccount
import com.example.storyapp.data.response.RegisterAccount
import com.example.storyapp.data.response.ResponseDetail
import com.example.storyapp.data.response.ResponseLogin
import java.lang.Exception

class MyRepository(private val apiService: ApiService) {

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

    suspend fun userLogin(loginAccount: LoginAccount): ResponseDetail {
        return try {
            val response = ApiConfig.getApiService().userLogin(loginAccount)
            if (response.error) {
                ResponseDetail(true, "Failed to login")
            } else {
                ResponseDetail(false, "Login success!")
            }
        } catch (e: Exception) {
            ResponseDetail(true, "Error message: ${e.message}")
        }
    }
}