package ru.smd.passnumber.data.entities

import com.google.gson.annotations.SerializedName

data class ResponseCheckCode(
    @SerializedName("id") val id: String,
    @SerializedName("client")val client:Client,
    @SerializedName("email")val email:String?,
    @SerializedName("phone")val phone:String,
    @SerializedName("name") val name:String,
    @SerializedName("roles")val roles:List<Roles>,
    @SerializedName("notification_time")val notification_time:String?,
    @SerializedName("notifications_email")val notifications_email:Boolean,
    @SerializedName("notifications_sms")val notifications_sms:Boolean,
    @SerializedName("notifications_push")val notifications_push:Boolean,
    @SerializedName("created_at")val createdAt:String
)