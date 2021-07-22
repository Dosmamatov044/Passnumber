package ru.smd.passnumber.data.service

data class ResponseData<T>(
    val status: String?,
    val data: T? = null,
    val message:String?
)