package ru.smd.passnumber.data.entities

import com.google.gson.annotations.SerializedName

data class ServerResponseCheckCode<T> (
    @SerializedName("data")  val data: T,
    @SerializedName("api_token")  val api_token:String
)