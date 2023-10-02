package com.example.storyapp.data.repository

import com.example.storyapp.data.api.ApiService
import com.example.storyapp.data.response.LoginAccount
import com.example.storyapp.data.response.RegisterAccount
import com.example.storyapp.data.response.ResponseDetail
import com.example.storyapp.data.response.ResponseLogin
import java.lang.Exception

class MyRepository(private val apiService: ApiService) {

    suspend fun userRegister(registerAccount: RegisterAccount): ResponseDetail {
        return apiService.userRegister(registerAccount)
    }

    suspend fun userLogin(loginAccount: LoginAccount): ResponseLogin {
        return apiService.userLogin(loginAccount)
    }
}