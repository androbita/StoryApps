package com.ngopidevteam.storyapps.di

import android.content.Context
import com.ngopidevteam.storyapps.data.UserRepository
import com.ngopidevteam.storyapps.data.pref.UserPreferences
import com.ngopidevteam.storyapps.data.pref.dataStore

object Injection {
    fun provideRepository(context: Context): UserRepository{
        val pref = UserPreferences.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }
}