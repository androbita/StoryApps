package com.ngopidevteam.storyapps.view.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ngopidevteam.storyapps.data.ResultState
import com.ngopidevteam.storyapps.data.UserRepository
import com.ngopidevteam.storyapps.remote.response.SignupResponse
import kotlinx.coroutines.launch

class SignupViewModel(private val repository: UserRepository): ViewModel() {

    private val _registerResult = MutableLiveData<ResultState<SignupResponse>>()
    val registerResult: LiveData<ResultState<SignupResponse>> = _registerResult

    fun register(name: String, email: String, password: String){
        _registerResult.value = ResultState.Loading
        viewModelScope.launch {
            val result = repository.registerUser(name, email, password)
            _registerResult.value = result
        }
    }
}