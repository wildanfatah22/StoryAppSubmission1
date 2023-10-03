package com.example.storyapp.data.api

import com.example.storyapp.data.response.DetailResponse
import com.example.storyapp.data.response.LoginAccount
import com.example.storyapp.data.response.LoginResponse
import com.example.storyapp.data.response.RegisterAccount
import com.example.storyapp.data.response.StoryResponse
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

//    @POST("register")
//    fun registerUser(@Body requestRegister: RegisterAccount): Response<DetailResponse>

//    @POST("login")
//    fun loginUser(@Body requestLogin: LoginAccount): Response<LoginResponse>

    @POST("register")
    fun registerUser(@Body requestRegister: RegisterAccount): Call<DetailResponse>

    @POST("login")
    fun loginUser(@Body requestLogin: LoginAccount): Call<LoginResponse>

    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") token: String
    ): Call<StoryResponse>

    @Multipart
    @POST("stories")
    suspend fun uploadPicture(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Header("Authorization") token: String
    ): Call<DetailResponse>
}