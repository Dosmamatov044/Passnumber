package ru.smd.passnumber.data.entities

import com.google.gson.annotations.SerializedName

data class LinksMeta(
    @SerializedName("url") val url: String?,
    @SerializedName("label")val label:String,
    @SerializedName("active")val active:Boolean
)