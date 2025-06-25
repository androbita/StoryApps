package com.ngopidevteam.storyapps.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ngopidevteam.storyapps.data.UserRepository
import com.ngopidevteam.storyapps.data.pref.UserModel
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository): ViewModel() {
    fun saveSession(user: UserModel){
//        viewModelScope.launch {
//            repository.saveSession(user)
//        }
    }
}