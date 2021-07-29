package ru.smd.passnumber.data.tools

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesHelper @Inject constructor(@ApplicationContext val context: Context) {

    private val sharedPreferences: SharedPreferences

    init {
        sharedPreferences = context.getSharedPreferences("mainPreferences", Context.MODE_PRIVATE)
    }

    fun storeToken(token:String?){
        sharedPreferences.edit().putString("token",token).apply()
    }

    fun restoreToken()=sharedPreferences.getString("token",null)

    fun clearToken()=sharedPreferences.edit().putString("token",null).apply()

}