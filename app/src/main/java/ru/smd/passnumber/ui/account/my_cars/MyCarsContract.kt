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

        fun showMoreCars(cars: List<PassData>)

        fun showEmptyList()

        fun enableEdtRegNum()

        fun setLastPage(lastPage:Int)
    }

    interface Presenter{

        fun onStart(view:View)

        fun onStop()

        fun onClickBack()

        fun onClickAdd()

        fun getMyCars()

        fun getMoreCars(page:Int)

        fun addCar(regNumber:String,labelModel:String,nameDriver:String)

        fun deleteCar(id: Int)
    }
}