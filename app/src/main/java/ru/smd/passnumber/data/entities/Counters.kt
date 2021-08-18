package ru.smd.passnumber.data.entities

import com.google.gson.annotations.SerializedName

data class Counters(@SerializedName("vehicles") val vehicles:Int,@SerializedName("unreadNotifications") val unreadNotifications:Int)