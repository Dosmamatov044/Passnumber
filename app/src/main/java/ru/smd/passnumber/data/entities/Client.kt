package ru.smd.passnumber.data.entities

import com.google.gson.annotations.SerializedName

data class Client (
    @SerializedName("id")val id:Int,
    @SerializedName("type")val type:Int,
    @SerializedName("inn")val inn:String?,
    @SerializedName("status")val status:String?,
    @SerializedName("rating")val rating:String?,
    @SerializedName("name")val name:String
        )