package ru.smd.passnumber.data.service

import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.Body

import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.QueryMap
import ru.smd.passnumber.data.entities.PassData
import ru.smd.passnumber.data.entities.Token


interface ApiService {

    @POST("vehicle/check")
    fun checkPassNumber(@Query("reg_number") reg_number: String): Call<ResponseData<PassData>>

    @POST("/auth/check_code")
    fun registration(@QueryMap params: MutableMap<String, String>): Single<ResponseData<Token>>
}