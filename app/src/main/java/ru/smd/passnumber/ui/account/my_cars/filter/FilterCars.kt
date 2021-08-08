package ru.smd.passnumber.ui.account.my_cars.filter

/**
 * Created by Siddikov Mukhriddin on 8/5/21
 */
data class FilterCars (
    var isFiltered:Boolean=false,
    var filterByStatus:String="",
    var filterByTypePass:String="",
    var filterBySort:Pair<String,Boolean> = Pair("",false)
)
data class FilterCarsPosition (
    var filterByStatus:Int=0,
    var filterByTypePass:Int=0,
    var filterBySort:Int=0
)