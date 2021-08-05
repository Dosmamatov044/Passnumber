package ru.smd.passnumber.service.firebase

//import retrofit2.Call
//import retrofit2.http.*
//
///**
// * Created by Siddikov Mukhriddin on 3/22/21
// */
//interface FirebaseApi{
//
//    /**
//     * push_token firebase
//     */
//    @Headers("Accept: application/json", "Content-Type: application/x-www-form-urlencoded")
//    @POST("firebase/token")
//    fun sendPushToken(
//        @Query("token") token: String,
//        @Query("uid") uid: String
//    ): Call<Any?>
//
//    /**
//     * send Feedback from local notification dialog
//     */
//    @Headers("Accept: application/json", "Content-Type: application/x-www-form-urlencoded")
//    @POST("feedback")
//    fun sendFeedback(
//        @Query("name") name: String,
//        @Query("email") email: String,
//        @Query("text") text: String,
//        @Query("grade") grade: Int
//    ): Call<Any?>
//
//
//
//}