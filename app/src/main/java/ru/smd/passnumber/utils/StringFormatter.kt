package ru.smd.passnumber.utils

import android.util.Log
import java.lang.Exception

/**
 * Created by Siddikov Mukhriddin on 7/22/21
 */

fun boldNumbers(string: String):String{
    val numberStart=string.indexOfFirst {
        it.isDigit()
    }
    val numberEnd=string.indexOfLast {
        it.isDigit()
    }
    var leftTime=string
    try{
        if (numberStart>0&&numberEnd>0)
        leftTime= string.substring(0,numberStart-1)+"<b>"+string.substring(numberStart,numberEnd+1)+"</b>"+string.substring(numberEnd+1,string.length)
    }
    catch (e: Exception){
        Log.e("TTT",e.toString())
    }
    return leftTime
}