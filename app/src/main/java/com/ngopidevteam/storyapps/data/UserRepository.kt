package com.ngopidevteam.storyapps.data

import com.ngopidevteam.storyapps.data.pref.UserModel
import com.ngopidevteam.storyapps.data.pref.UserPreferences
import com.ngopidevteam.storyapps.remote.response.LoginResponse
import com.ngopidevteam.storyapps.remote.response.SignupResponse
import com.ngopidevteam.storyapps.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow

class UserRepository(
    private val userPreferences: UserPreferences,
    private val apiService: ApiService
){

    //register user
    suspend fun registerUser(name: String, email: String, password: String): ResultState<SignupResponse>{
        return try {
            val response = apiService.register(name, email, password)
            ResultState.Success(response)
        }catch (e: Exception){
            ResultState.Error(e.message ?: "unexpected error")
        }
    }

//    login user dan simpan token ke datastore
    suspend fun loginUser(email: String, password: String): ResultState<LoginResponse>{
        return try {
            val response = apiService.login(email, password)

            //simpan sebagai userModel
            val user = UserModel(
                email = email,
                token = response.loginResult?.token.toString(),
                isLogin = true
            )

            //simpan token
            userPreferences.saveSession(user)
            ResultState.Success(response)

        }catch (e: Exception){
            ResultState.Error(e.message ?: "Unexpected error occured during login")
        }
    }

    companion object{
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(
            userPreferences: UserPreferences,
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this){
                instance ?: UserRepository(userPreferences, apiService)
            }.also { instance = it }
    }

//    suspend fun saveSession(user: UserModel){
//        userPreferences.saveSession(user)
//    }

    fun getSession(): Flow<UserModel>{
        return userPreferences.getSession()
    }

    suspend fun logout(){
        userPreferences.logout()
    }
}