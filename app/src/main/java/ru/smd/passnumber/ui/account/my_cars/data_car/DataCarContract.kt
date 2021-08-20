package ru.smd.passnumber.ui.account.my_cars.data_car

interface DataCarContract {
    interface View{

        fun toBack()

        fun exit()

        fun showPasses(regNumber: String)

        fun showDataCar()

        fun showData(mark: String,driverName: String,regNumber: String)

        fun showDocs(idVehicle:Int)

        fun showRecommendation(show:Boolean)

        fun showNotificationsForCar(id:Int)

        fun showAlertNotification(show: Boolean,count:String)
    }

    interface Presenter{



        fun onStart(view:View)

        fun getData(regNumber: String)

        fun onStop()

        fun getDocs(idVehicle:Int)

        fun onClickBack()

        fun onClickDataCar()

        fun onClickSaveData(mark:String,driverName:String,regNumber:String)

        fun onClickDocs()

        fun onClickNotifications()
    }
}