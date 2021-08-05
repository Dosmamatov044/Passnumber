package ru.smd.passnumber.ui.account.my_cars.data_car

import ru.smd.passnumber.data.entities.PassData

interface DataCarContract {
    interface View{

        fun toBack()

        fun exit()

        fun showDataCar()

        fun showData(mark: String,driverName: String,regNumber: String)

        fun showDocs(idVehicle:Int)
    }

    interface Presenter{

        fun onStart(view:View)

        fun getData(regNumber: String)

        fun onStop()

        fun onClickBack()

        fun onClickDataCar()

        fun onClickSaveData(mark:String,driverName:String,regNumber:String)

        fun onClickDocs()
    }
}