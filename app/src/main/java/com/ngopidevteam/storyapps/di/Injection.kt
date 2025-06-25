package com.ngopidevteam.storyapps.di

import android.content.Context
import com.ngopidevteam.storyapps.data.UserRepository
import com.ngopidevteam.storyapps.data.pref.UserPreferences
import com.ngopidevteam.storyapps.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): UserRepository{
        val userPreferences = UserPreferences.getInstance(context)
        val apiService = ApiConfig.getApiService()
        return UserRepository(userPreferences, apiService)
    }

    fun providePreferences(context: Context): UserPreferences{
        return UserPreferences.getInstance(context)
    }
}