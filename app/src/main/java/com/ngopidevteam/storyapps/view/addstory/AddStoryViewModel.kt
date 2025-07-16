package com.ngopidevteam.storyapps.view.addstory

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ngopidevteam.storyapps.data.ResultState
import com.ngopidevteam.storyapps.data.UserRepository
import com.ngopidevteam.storyapps.reduceImageFile
import com.ngopidevteam.storyapps.remote.retrofit.ApiConfig
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File

class AddStoryViewModel(
    private val repository: UserRepository
): ViewModel() {

    private val _uploadState = MutableLiveData<ResultState<String>>()
    val uploadState: LiveData<ResultState<String>> = _uploadState

    fun uploadStory(token: String, description: RequestBody, imgFile: MultipartBody.Part) {
        viewModelScope.launch {
            _uploadState.value = ResultState.Loading
            viewModelScope.launch {
                val result = repository.uploadStory(token, description, imgFile)
                _uploadState.value = result
            }

//            val descBody = description.toRequestBody("text/plain".toMediaType())
//            val imageBody = compressed.asRequestBody("image/jpeg".toMediaType())
//            val imageMultipart = MultipartBody.Part.createFormData(
//                "photo",
//                compressed.name,
//                imageBody
//            )
//
//            _uploadState.value = ResultState.Loading
//
//            try {
//                val api = ApiConfig.getApiService(token)
//                val response = api.uploadStory(descBody, imageMultipart)
//
//                if (response.error == false) {
//                    _uploadState.value = ResultState.Success(response.message.toString())
//                } else {
//                    _uploadState.value = ResultState.Error(response.message.toString())
//                }
//            } catch (e: Exception) {
//                _uploadState.value = ResultState.Error(e.message ?: "Upload gagal")
//            }
        }
    }
}