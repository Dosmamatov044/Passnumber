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

    fun getFirstAddedCar():Boolean{
        return sharedPreferences.getBoolean("isFirstAddedCar",false)
    }
    fun setFirstAddedCar(){
        sharedPreferences.edit().putBoolean("isFirstAddedCar",true).apply()
    }

    fun storeToken(token:String?){
        sharedPreferences.edit().putString("token",token).apply()
    }

    fun storeCarRegistration(regNumber: String,mark:String,driverName:String){
        sharedPreferences.edit().putString("regNumber",regNumber).apply()
        sharedPreferences.edit().putString("mark",mark).apply()
        sharedPreferences.edit().putString("driverName",driverName).apply()
    }

    fun restoreCarRegistrationRegNumber()=sharedPreferences.getString("regNumber","")

    fun restoreCarRegistrationMark()=sharedPreferences.getString("mark","")

    fun restoreCarRegistrationDriverName()=sharedPreferences.getString("driverName","")



    fun clearCarRegistration(){
        sharedPreferences.edit().putString("regNumber","").apply()
        sharedPreferences.edit().putString("mark","").apply()
        sharedPreferences.edit().putString("driverName","").apply()
    }

    fun restoreToken()=sharedPreferences.getString("token",null)

    fun clearToken()=sharedPreferences.edit().putString("token",null).apply()

    fun storeFio(fio:String){
        sharedPreferences.edit().putString("fio",fio).apply()
    }
    fun restoreFio()=sharedPreferences.getString("fio","")

    fun clearFio()=sharedPreferences.edit().putString("fio",null).apply()

    fun storePhone(phone:String){
        sharedPreferences.edit().putString("phone",phone).apply()
    }
    fun restorePhone()=sharedPreferences.getString("phone","")

    fun clearPhone()=sharedPreferences.edit().putString("phone",null).apply()

    fun storeCompany(company:String){
        sharedPreferences.edit().putString("company",company).apply()
    }
    fun restoreCompany()=sharedPreferences.getString("company","")

    fun clearCompany()=sharedPreferences.edit().putString("company",null).apply()

    fun storeEmail(email:String?){
        sharedPreferences.edit().putString("email",email).apply()
    }
    fun restoreEmail()=sharedPreferences.getString("email","")

    fun clearEmail()=sharedPreferences.edit().putString("email",null).apply()
}