package ru.smd.passnumber.data.entities

import com.google.gson.annotations.SerializedName

data class Roles(
    @SerializedName("slug")val slug:String,
    @SerializedName("name")val name:String
)