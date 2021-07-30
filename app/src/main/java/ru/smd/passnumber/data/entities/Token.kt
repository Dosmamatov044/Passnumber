package ru.smd.passnumber.data.entities

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName


data class Token (@SerializedName("api_token") val token: String)
