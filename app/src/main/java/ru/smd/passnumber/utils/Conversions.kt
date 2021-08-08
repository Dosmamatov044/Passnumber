package ru.smd.passnumber.utils

/**
 * Created by Siddikov Mukhriddin on 8/5/21
 */

fun Boolean.toInt():Int{
   return if (this)
       1
    else 0
}