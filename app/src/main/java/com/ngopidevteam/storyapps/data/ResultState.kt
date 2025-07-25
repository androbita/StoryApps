package com.ngopidevteam.storyapps.data

sealed class ResultState<out R> {
    data class Success<out T>(val data: T) : ResultState<T>()
    data class Error(val message: String) : ResultState<Nothing>()
    object Loading : ResultState<Nothing>()
}