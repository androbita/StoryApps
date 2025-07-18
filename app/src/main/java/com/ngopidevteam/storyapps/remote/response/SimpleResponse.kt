package com.ngopidevteam.storyapps.remote.response

import com.google.gson.annotations.SerializedName

data class SimpleResponse(

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)
