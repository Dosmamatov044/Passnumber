package ru.smd.passnumber.data.entities

import ru.smd.passnumber.utils.getDateWithServerTimeStamp
import ru.smd.passnumber.utils.getStringTimeStampWithDate


/**
 * Created by Siddikov Mukhriddin on 2/13/21
 */

data class PassData(
val id:Int,
val reg_number:String?,
val passes:List<PassesData>
)

data class PassesData(
    val number: String?,
    val valid_from: String?,
    val valid_to: String?,
    val cancel_date: String?,
    val area: String?,
    val status: String?,
    val days_left: Int?,
    val validity_period: String?
){
    fun getTime():String{
        val validFrom=  valid_from?.getDateWithServerTimeStamp()?.getStringTimeStampWithDate()
        val validTo=  valid_to?.getDateWithServerTimeStamp()?.getStringTimeStampWithDate()
        return  "с <b>$validFrom</b> по <b>$validTo</b>"
    }
}