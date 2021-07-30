package ru.smd.passnumber.data.entities

import com.google.gson.annotations.SerializedName

data class Meta(
    @SerializedName("current_page") val currentPage: String,
    @SerializedName("from") val from: String,
    @SerializedName("last_page") val lastPage: String,
    @SerializedName("links") val links: List<LinksMeta>,
    @SerializedName("path") val path: String,
    @SerializedName("per_page") val perPage: String,
    @SerializedName("to") val to: String,
    @SerializedName("total") val total: String
)