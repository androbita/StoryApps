package com.ngopidevteam.storyapps.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ngopidevteam.storyapps.data.ResultState
import com.ngopidevteam.storyapps.data.UserRepository
import com.ngopidevteam.storyapps.data.model.UserModel
import com.ngopidevteam.storyapps.remote.response.LoginResponse
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<ResultState<LoginResponse>>()
    val loginResult: LiveData<ResultState<LoginResponse>> = _loginResult

    //liveData untuk userModel
    val user: LiveData<UserModel> = repository.getSession().asLiveData()

    fun loginUser(email: String, password: String) {
        _loginResult.value = ResultState.Loading
        viewModelScope.launch {
            try {
                val result = repository.loginUser(email, password)
                _loginResult.value = result

                if (result is ResultState.Success) {
                    val loginData = result.data.loginResult
                    val user = UserModel(
                        name = loginData?.name.toString(),
                        token = loginData?.token.toString(),
                        isLogin = true,
                        userId = loginData?.userId.toString(),
                        email = email
                    )

                    repository.saveSession(user)
                    repository.updateApiService(loginData?.token.toString())
                }
            }catch (e: Exception){
                _loginResult.value = ResultState.Error(e.message ?: "Terjadi kesalahan")
            }
        }
    }

//    fun saveSession(user: UserModel){
//        viewModelScope.launch {
//            repository.saveSession(user)
//        }
//    }
    }