package ru.smd.passnumber.ui.account.my_cars.data_car.docs

import ru.smd.passnumber.data.entities.Docs

interface DocsContract {
    interface View{

        fun toBack()

        fun showDocs(docs:List<Docs>)
    }

    interface Presenter{

        fun onStart(view:View)

        fun onStop()

        fun onClickBack()

        fun getDocs(idVehicle:Int)
    }
}