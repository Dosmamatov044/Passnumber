package ru.smd.passnumber.data.service

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Single
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.QueryMap
import ru.smd.passnumber.BuildConfig
import ru.smd.passnumber.data.core.Constants
import ru.smd.passnumber.data.entities.ResponseCheckCode
import ru.smd.passnumber.data.entities.ResponseNotifications
import ru.smd.passnumber.data.entities.ServerResponseCheckCode
import ru.smd.passnumber.data.tools.PreferencesHelper
import javax.inject.Inject

interface PassNumberRepo {


    @POST("auth/check_code")
    fun registration(@QueryMap params: MutableMap<String, String>): Single<ServerResponseCheckCode<ResponseCheckCode>>

    @POST("auth/send_code")
    fun getCode(@Query("phone") phone: String): Single<ResponseData<Any>>

    @POST("user")
    fun saveDataUser(@QueryMap params: MutableMap<String, String>): Single<ResponseData<Any>>

    @GET("user")
    fun getUser():Single<ServerResponseCheckCode<ResponseCheckCode>>

    @GET("notifications")
    fun getNotifications():Single<ResponseNotifications>

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
            addConverterFactory(GsonConverterFactory.create())
            addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            build().create(PassNumberRepo::class.java)
        }
    }
}