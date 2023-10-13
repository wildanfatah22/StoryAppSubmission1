package com.example.storyapp.di

import android.content.Context
import com.example.storyapp.data.api.ApiConfig
import com.example.storyapp.data.local.datastore.AppDb
import com.example.storyapp.data.repository.UserRepository

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val database = AppDb.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return UserRepository(database, apiService)
    }
}