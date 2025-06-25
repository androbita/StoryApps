package com.ngopidevteam.storyapps.data

import com.ngopidevteam.storyapps.remote.response.SignupResponse
import com.ngopidevteam.storyapps.remote.retrofit.ApiService

class UserRepository(
//    private val userPreferences: UserPreferences
    private val apiService: ApiService
){

    suspend fun registerUser(name: String, email: String, password: String): ResultState<SignupResponse>{
        return try {
            val response = apiService.register(name, email, password)
            ResultState.Success(response)
        }catch (e: Exception){
            ResultState.Error(e.message ?: "unexpected error")
        }
    }

    companion object{
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(
//            userPreferences: UserPreferences
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this){
                instance ?: UserRepository(apiService)
            }.also { instance = it }
    }

//    suspend fun saveSession(user: UserModel){
//        userPreferences.saveSession(user)
//    }
//
//    fun getSession(): Flow<UserModel>{
//        return userPreferences.getSession()
//    }
//
//    suspend fun logout(){
//        userPreferences.logout()
//    }
}