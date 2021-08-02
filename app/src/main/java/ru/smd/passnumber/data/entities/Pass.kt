package ru.smd.passnumber.data.entities

import com.google.gson.annotations.SerializedName

data class Pass(
    @SerializedName("number") val number: String,
    @SerializedName("reg_number")val regNumber:String,
    @SerializedName("valid_from")val validFrom:String,
    @SerializedName("valid_to")val validTo:String,
    @SerializedName("cancel_date")val cancelDate:String?,
    @SerializedName("area")val area:String,
    @SerializedName("status")val status:String,
    @SerializedName("days_left")val daysLeft:String,
    @SerializedName("validity_period")val validityPeriod:String
)