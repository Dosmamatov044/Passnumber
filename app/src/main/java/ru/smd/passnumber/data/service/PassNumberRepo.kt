package ru.smd.passnumber.data.service

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Single
import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import ru.smd.passnumber.BuildConfig
import ru.smd.passnumber.data.core.Constants
import ru.smd.passnumber.data.entities.*
import ru.smd.passnumber.data.tools.PreferencesHelper
import javax.inject.Inject

interface PassNumberRepo {

    @POST("user/notifications")
    fun saveNotifications(
//        @Query("notification_time") notification_time: String,
        @Query("notifications_email") notifications_email: Int,
        @Query("notifications_push") notifications_push: Int
    ): Single<Unit>


    @POST("auth/check_code")
    fun registration(@QueryMap params: MutableMap<String, String>): Single<ServerResponseCheckCode<ResponseCheckCode>>

    @POST("auth/send_code")
    fun getCode(@Query("phone") phone: String): Single<ResponseData<Any>>

    @POST("user")
    fun saveDataUser(@QueryMap params: MutableMap<String, String>): Single<ResponseData<Any>>

    @GET("user")
    fun getUser(): Single<ServerResponseCheckCode<ResponseCheckCode>>

    @GET("notifications")
    fun getNotifications(): Single<ResponseNotifications>

    @GET("vehicle")
    fun getCarList(@Query("page") page: Int): Single<ResponseVehicle>

    @DELETE("vehicle/{vehicle_id}")
    fun deleteCard(@Path("vehicle_id") vehicle_id: Int): Single<Unit>

    @DELETE("document/{document_id}")
    fun deleteDoc(@Path("document_id") document_id: Int): Single<Unit>

    @POST("vehicle")
    fun addCar(@QueryMap params: MutableMap<String, String>): Single<PassData>


    @POST("vehicle/check")
    fun checkPassNumber(@Query("reg_number") reg_number: String): Single<ResponseData<PassData>>

    @GET("vehicle/{vehicle_id}/documents")
    fun getDocs(@Path("vehicle_id") vehicle_id: Int): Single<ResponseData<List<Docs>>>

    @GET("user/counters")
    fun getCounters(): Single<Counters>

    @GET("notifications/vehicle/{vehicle_id}")
    fun getNotificationForCar(@Path("vehicle_id") vehicle_id: Int): Single<ResponseNotifications>

    @POST("notifications/{id}/read")
    fun readNotification(@Path("id") id: String): Single<ResponseData<Any>>

    @GET("notifications/unread")
    fun getUnreadNotifications(): Single<ResponseNotifications>

    @GET("notifications/vehicle/{vehicle_id}/unread")
    fun getUnreadNotificationsForCar(@Path("vehicle_id") vehicle_id:Int):Single<ResponseNotifications>

    @Multipart
    @POST("vehicle/{vehicle_id}/documents")
    fun storeDocs(
        @Path("vehicle_id") vehicle_id: Int,
        @Part file: MultipartBody.Part,
        @PartMap params: MutableMap<String, RequestBody>
    ): Single<ResponseData<Any>>

    class Factory @Inject constructor(val preferencesHelper: PreferencesHelper) {
        fun create() = Retrofit.Builder().run {

            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            val okHttpBuilder = OkHttpClient.Builder()
            okHttpBuilder.addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain) = chain.run {
                    proceed(request()
                        .newBuilder()
                        .run {
                            preferencesHelper.restoreToken()?.let {
                                addHeader("Authorization", "Bearer $it")
                            }
                            addHeader("User-Agent", "Android")

                            build()
                        }
                    )
                }
            })

            if (BuildConfig.DEBUG) {
//                client(OkHttpClient.Builder().addInterceptor(interceptor).build())
                okHttpBuilder.addInterceptor(httpLoggingInterceptor)
            }
//            okHttpBuilder.addInterceptor(Interceptor { chain ->
//                val requestChain = chain.request()
//                val requestBuilder = requestChain.newBuilder()
//                val token=preferencesHelper.restoreToken()
//              //  if (token!=null){
//                //    requestBuilder.addHeader("Authorization",
//             //          "Bearer $token"
//             //       )
//            //    }
//              //  requestBuilder.addHeader("Content-Type","application/json")
//                chain.proceed(requestBuilder.build())
//            })
            client(okHttpBuilder.build())
            baseUrl(Constants.BASE_URL)
            addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            build().create(PassNumberRepo::class.java)
        }
    }
}