package ru.smd.passnumber.data.entities

import com.google.gson.annotations.SerializedName

data class ResponseVehicle(
    @SerializedName("data") val data:List<PassData>,
    @SerializedName("links") val links: Links,
    @SerializedName("meta")val meta:Meta
)