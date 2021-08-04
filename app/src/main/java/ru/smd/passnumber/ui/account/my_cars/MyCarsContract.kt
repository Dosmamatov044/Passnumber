package ru.smd.passnumber.ui.account.my_cars

import ru.smd.passnumber.data.entities.PassData

interface MyCarsContract {
    interface View{

        fun showErrorMessage(message: String)

        fun showErrorInternet()

        fun toBack()

        fun exit()

        fun showAddCarBlock()

        fun showCarList(cars: List<PassData>, firstAddedCar: Boolean)

        fun showEmptyList()

        fun enableEdtRegNum()
    }

    interface Presenter{

        fun onStart(view:View)

        fun onStop()

        fun onClickBack()

        fun onClickAdd()

        fun getMyCars()

        fun addCar(regNumber:String,labelModel:String,nameDriver:String)
        fun deleteCar(regNumber: Int)
    }
}