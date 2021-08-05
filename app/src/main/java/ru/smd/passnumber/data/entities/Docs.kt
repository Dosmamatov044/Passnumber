package ru.smd.passnumber.data.entities

import com.google.gson.annotations.SerializedName

data class Docs(
    @SerializedName("id") val id: String,
    @SerializedName("type_id") val type: String,
    @SerializedName("status_id") val status: String,
    @SerializedName("file")val file:String,
    @SerializedName("thumb")val thumb:String?
)