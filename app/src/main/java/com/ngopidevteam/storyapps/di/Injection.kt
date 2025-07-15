package com.ngopidevteam.storyapps.di

import android.content.Context
import com.ngopidevteam.storyapps.data.UserRepository
import com.ngopidevteam.storyapps.data.pref.UserPreferences
import com.ngopidevteam.storyapps.remote.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository{
        val userPreferences = UserPreferences.getInstance(context)
        val user = runBlocking { userPreferences.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return UserRepository(userPreferences, apiService)
    }
}