package ru.smd.passnumber.data.entities

import com.google.gson.annotations.SerializedName

data class ResponseNotifications(
    @SerializedName("data") val data:List<Notification>,
    @SerializedName("links") val links: Links,
    @SerializedName("meta")val meta:Meta
)