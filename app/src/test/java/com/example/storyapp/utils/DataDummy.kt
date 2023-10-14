package com.example.storyapp.utils

import com.example.storyapp.data.local.entity.StoryDetailResponse
import com.example.storyapp.data.response.LoginAccount
import com.example.storyapp.data.response.LoginResponse
import com.example.storyapp.data.response.LoginResult
import com.example.storyapp.data.response.RegisterAccount

object DataDummy {

    fun generateDummyStoryEntity(): List<StoryDetailResponse> {
        val newsList = ArrayList<StoryDetailResponse>()
        for (i in 0..5) {
            val stories = StoryDetailResponse(
                "Title $i",
                "this is name",
                "This is description",
                "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
                "2022-02-22T22:22:22Z",
                null,
                null,
            )
            newsList.add(stories)
        }
        return newsList
    }

    fun generateDummyNewStories(): List<StoryDetailResponse> {
        val newStoryList = ArrayList<StoryDetailResponse>()
        for (i in 0..5) {
            val stories = StoryDetailResponse(
                "Title $i",
                "this is name",
                "This is description",
                "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
                "2022-02-22T22:22:22Z",
                null,
                null,
            )
            newStoryList.add(stories)
        }
        return newStoryList
    }


    fun generateDummyRequestLogin(): LoginAccount {
        return LoginAccount("awikwok@gmail.com", "awikwokk")
    }

    fun generateDummyResponseLogin(): LoginResponse {
        val newLoginResult = LoginResult("awikwok", "wildan", "ini-token")
        return LoginResponse(false, "Login successfully", newLoginResult)
    }

    fun generateDummyRequestRegister(): RegisterAccount {
        return RegisterAccount("wizz", "wizz22@gmail.com", "12345678")
    }

}