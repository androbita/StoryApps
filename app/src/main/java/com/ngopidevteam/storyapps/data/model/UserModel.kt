package com.ngopidevteam.storyapps.data.model

data class UserModel(
    val email: String,
    val token: String,
    val isLogin: Boolean = false,
    val userId: String,
    val name: String
)