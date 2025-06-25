package com.ngopidevteam.storyapps.di

import com.ngopidevteam.storyapps.data.UserRepository
import com.ngopidevteam.storyapps.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(): UserRepository{
        val apiService = ApiConfig.getApiService()
        return UserRepository(apiService)
    }
}