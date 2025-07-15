package com.ngopidevteam.storyapps.view.addstory

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ngopidevteam.storyapps.data.ResultState
import com.ngopidevteam.storyapps.data.UserRepository
import com.ngopidevteam.storyapps.remote.retrofit.ApiConfig
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File

class AddStoryViewModel(
    private val repository: UserRepository
): ViewModel() {

    private val _uploadState = MutableLiveData<ResultState<String>>()
    private val uploadState: LiveData<ResultState<String>> = _uploadState

    fun uploadStory(token: String, description: String, imgFile: File) {
        viewModelScope.launch {
            val compressed = reduceFileImage(imgFile)
            if (compressed == null) {
                _uploadState.value = ResultState.Error("Ukuran file melebihi 1MB setelah dikompres")
                return@launch
            }

            val descBody = description.toRequestBody("text/plain".toMediaType())
            val imageBody = compressed.asRequestBody("image/jpeg".toMediaType())
            val imageMultipart = MultipartBody.Part.createFormData(
                "photo",
                compressed.name,
                imageBody
            )

            _uploadState.value = ResultState.Loading

            try {
                val api = ApiConfig.getApiService(token)
                val response = api.uploadStory(descBody, imageMultipart)

                if (response.error == false) {
                    _uploadState.value = ResultState.Success(response.message.toString())
                } else {
                    _uploadState.value = ResultState.Error(response.message.toString())
                }
            } catch (e: Exception) {
                _uploadState.value = ResultState.Error(e.message ?: "Upload gagal")
            }
        }
    }

    private fun reduceFileImage(file: File): File? {
        var compressQuality = 100
        var streamLength: Int

        do {
            val bmp = BitmapFactory.decodeFile(file.path)
            val stream = ByteArrayOutputStream()
            bmp.compress(Bitmap.CompressFormat.JPEG, compressQuality, stream)
            val byteArray = stream.toByteArray()
            streamLength = byteArray.size
            compressQuality -= 5
        } while (streamLength > 1000000 && compressQuality > 5)

        if (streamLength > 1000000) return null

        file.writeBytes(ByteArrayOutputStream().apply {
            BitmapFactory.decodeFile(file.path)
                .compress(Bitmap.CompressFormat.JPEG, compressQuality, this)
        }.toByteArray())

        return file
    }
}