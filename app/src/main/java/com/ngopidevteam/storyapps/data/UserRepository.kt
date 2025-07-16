package com.ngopidevteam.storyapps.data

import com.google.gson.Gson
import com.ngopidevteam.storyapps.data.model.UserModel
import com.ngopidevteam.storyapps.data.pref.UserPreferences
import com.ngopidevteam.storyapps.remote.response.ErrorResponse
import com.ngopidevteam.storyapps.remote.response.LoginResponse
import com.ngopidevteam.storyapps.remote.response.SimpleResponse
import com.ngopidevteam.storyapps.remote.response.StoryResponse
import com.ngopidevteam.storyapps.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class UserRepository(
    private val userPreferences: UserPreferences,
    private val apiService: ApiService
){

    //register user
    suspend fun registerUser(name: String, email: String, password: String): ResultState<SimpleResponse>{
        return try {
            val response = apiService.register(name, email, password)
            ResultState.Success(response)
        }catch (e: HttpException){
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            ResultState.Error(errorMessage ?: "unexpected error")
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
                isLogin = true,
                userId = response.loginResult?.userId.toString(),
                name = response.loginResult?.name.toString()
            )

            //simpan token
            userPreferences.saveSession(user)
            ResultState.Success(response)

        }catch (e: HttpException){
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            ResultState.Error(errorMessage ?: "Unexpected error occured during login")
        }
    }

    //get story
    suspend fun getStories(
        page: Int? = null,
        size: Int? = null,
        location: Int = 0
    ): ResultState<StoryResponse> {
        return try {
            val response = apiService.getStories(page, size, location)
            ResultState.Success(response)
        }catch (e: HttpException){
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            ResultState.Error(errorMessage ?: "unexpected error")
        }
    }

    //upload story
    suspend fun uploadStory(
        token: String,
        description: RequestBody,
        photo: MultipartBody.Part
    ): ResultState<String>{
        return try {
            val response = apiService.uploadStory(description, photo)
            if (response.error == false){
                ResultState.Success(response.message.toString())
            }else{
                ResultState.Error(response.message.toString())
            }
        }catch (e: Exception){
            ResultState.Error(e.message ?: "Upload Gagal")
        }
    }

    //logout
    suspend fun logout(){
        userPreferences.logout()
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

    fun getSession(): Flow<UserModel>{
        return userPreferences.getSession()
    }
}