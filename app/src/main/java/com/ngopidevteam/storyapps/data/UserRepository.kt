package com.ngopidevteam.storyapps.data

import com.ngopidevteam.storyapps.data.pref.UserModel
import com.ngopidevteam.storyapps.data.pref.UserPreferences
import kotlinx.coroutines.flow.Flow

class UserRepository private constructor(
    private val userPreferences: UserPreferences
){

    companion object{
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(
            userPreferences: UserPreferences
        ): UserRepository =
            instance ?: synchronized(this){
                instance ?: UserRepository(userPreferences)
            }.also { instance = it }
    }

    suspend fun saveSession(user: UserModel){
        userPreferences.saveSession(user)
    }

    fun getSession(): Flow<UserModel>{
        return userPreferences.getSession()
    }

    suspend fun logout(){
        userPreferences.logout()
    }
}