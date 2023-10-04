package com.example.storyapp.data.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class DetailResponse(
	var error: Boolean,
	var message: String
)

data class RegisterAccount(
	var name: String,
	var email: String,
	var password: String
)

data class LoginAccount(
	var email: String,
	var password: String
)

data class LoginResponse(
	var error: Boolean,
	var message: String,
	var loginResult: LoginResult
)

data class LoginResult(
	var userId: String,
	var name: String,
	var token: String
)

data class StoryResponse(
	var error: String,
	var message: String,
	var listStory: ArrayList<DetailStory>
)

@Parcelize
data class DetailStory(
	var id: String,
	var name: String,
	var description: String,
	var photoUrl: String,
	var createdAt: String,
	var lat: Double,
	var lon: Double
) : Parcelable