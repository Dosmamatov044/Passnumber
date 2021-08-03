package ru.smd.passnumber.data.entities

import com.google.gson.annotations.SerializedName
import ru.smd.passnumber.utils.getDateWithServerTimeStamp
import ru.smd.passnumber.utils.getStringTimeStampWithDate


/**
 * Created by Siddikov Mukhriddin on 2/13/21
 */

data class PassData(
    @SerializedName("id") val id:Int,
    @SerializedName("reg_number") val regNumber:String?,
    @SerializedName("mark") val mark:String?,
    @SerializedName("driver_name") val driverName:String?,
    @SerializedName("passes") val passes:List<PassesData>
)

data class PassesData(
    @SerializedName("number") val number: String?,
    @SerializedName("reg_number") val regNumber: String?,
    @SerializedName("valid_from")  val validFrom: String?,
    @SerializedName("valid_to")  val validTo: String?,
    @SerializedName("cancel_date") val cancelDate:String?,
    @SerializedName("area")    val area: String?,
    @SerializedName("status")  val status: String?,
    @SerializedName("days_left")  val daysLeft: Int?,
    @SerializedName("validity_period")  val validityPeriod: String?
){
    fun getTime():String{
        val validFrom=  validFrom?.getDateWithServerTimeStamp()?.getStringTimeStampWithDate()
        val validTo=  validTo?.getDateWithServerTimeStamp()?.getStringTimeStampWithDate()
        return  "с <b>$validFrom</b> по <b>$validTo</b>"
    }
}