package com.example.storyapp.data.api

import com.example.storyapp.data.response.LoginAccount
import com.example.storyapp.data.response.RegisterAccount
import com.example.storyapp.data.response.ResponseDetail
import com.example.storyapp.data.response.ResponseLogin
import com.example.storyapp.data.response.ResponseStory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {

    @POST("register")
    suspend fun userRegister(@Body requestRegister: RegisterAccount): ResponseDetail

    @POST("login")
    suspend fun userLogin(@Body requestLogin: LoginAccount): ResponseLogin

    @GET("stories")
    fun getStory(
        @Header("Authorization") token: String,
    ): ResponseStory

    @Multipart
    @POST("stories")
    fun uploadPicture(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Header("Authorization") token: String
    ): ResponseDetail
}