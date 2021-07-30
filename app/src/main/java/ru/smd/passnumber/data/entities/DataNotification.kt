package ru.smd.passnumber.data.entities

import com.google.gson.annotations.SerializedName

data class DataNotification(
    @SerializedName("title") val title: String,
    @SerializedName("body") val body: String
)