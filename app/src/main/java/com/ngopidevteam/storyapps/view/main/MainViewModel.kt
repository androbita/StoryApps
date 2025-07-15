package com.ngopidevteam.storyapps.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ngopidevteam.storyapps.data.ResultState
import com.ngopidevteam.storyapps.data.UserRepository
import com.ngopidevteam.storyapps.data.model.UserModel
import com.ngopidevteam.storyapps.remote.response.StoryResponse
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel(){

    private val _storiesResult = MutableLiveData<ResultState<StoryResponse>>()
    val storiesResult: LiveData<ResultState<StoryResponse>> = _storiesResult

    fun getStories(page: Int? = null, size: Int? = null, location: Int = 0){
        _storiesResult.value = ResultState.Loading
        viewModelScope.launch {
            val result = repository.getStories(page, size, location)
            _storiesResult.value = result
        }
    }

    fun getSession(): LiveData<UserModel>{
        return repository.getSession().asLiveData()
    }

    fun logout(){
        viewModelScope.launch {
            repository.logout()
        }
    }
}