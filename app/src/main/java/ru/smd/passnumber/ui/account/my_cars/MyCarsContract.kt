package ru.smd.passnumber.ui.account.my_cars

interface MyCarsContract {
    interface View{

        fun showErrorMessage(message: String)

        fun showErrorInternet()

        fun toBack()

        fun exit()

        fun showAddCarBlock()

        fun showCarList()

        fun showEmptyList()
    }

    interface Presenter{

        fun onStart(view:View)

        fun onStop()

        fun onClickBack()

        fun onClickAdd()

        fun getMyCars()

        fun addCar(regNumber:String,labelModel:String,nameDriver:String)
    }
}