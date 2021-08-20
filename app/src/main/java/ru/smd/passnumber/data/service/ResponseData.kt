package ru.smd.passnumber.data.service

import com.google.gson.annotations.SerializedName

data class ResponseData<T>(
    val status: String?,
    val data: T? = null,
    val message:String?,
    @SerializedName("is_saved") val is_saved:Boolean?
)