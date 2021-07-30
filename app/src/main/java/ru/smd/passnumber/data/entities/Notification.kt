package ru.smd.passnumber.data.entities

import com.google.gson.annotations.SerializedName

data class Notification(
    @SerializedName("id") val id: String,
    @SerializedName("type") val type: String,
    @SerializedName("data") val data: DataNotification,
    @SerializedName("read_at") val readAt: String,
    @SerializedName("diff") val diff: String,
)