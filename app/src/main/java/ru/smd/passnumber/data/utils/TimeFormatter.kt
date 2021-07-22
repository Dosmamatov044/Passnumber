package ru.smd.passnumber.data.utils

import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Siddikov Mukhriddin on 7/22/21
 */
/** Converting from String to Date **/
fun String.getDateWithServerTimeStamp(): Date? {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
        Locale.getDefault())
    dateFormat.timeZone = TimeZone.getTimeZone("GMT")  // IMP !!!
    try {
        return dateFormat.parse(this)
    } catch (e: ParseException) {
        return null
    }

}
/** Converting from Date to String**/
fun Date.getStringTimeStampWithDate(): String {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy",
        Locale.getDefault())
    dateFormat.timeZone = TimeZone.getTimeZone("GMT")
    return dateFormat.format(this)
}

fun test(){
    /** TESTING **/
    val dateString = "2018-01-09T07:06:41.747Z"
    val date = dateString.getDateWithServerTimeStamp()
    Log.d("LOG", "String To Date Conversion " +date.toString())
    val dateBackToString = date?.getStringTimeStampWithDate()
    Log.d("LOG", "Date To String Conversion " +dateBackToString)
    /** OUTPUT **/
//    String To Date Conversion Tue Jan 09 15:06:41 GMT+08:00 2018
//    Date To String Conversion 2018-01-09T07:06:41.747Z
    /** NOTE: I am currently at GMT+08:00 **/
}
