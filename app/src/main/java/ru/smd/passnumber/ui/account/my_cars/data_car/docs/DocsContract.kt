package ru.smd.passnumber.ui.account.my_cars.data_car.docs

import ru.smd.passnumber.data.entities.Docs

interface DocsContract {
    interface View{

        fun toBack()

        fun showDocsSts(docs:List<Docs>)

        fun showDocsPts(docs:List<Docs>)
        fun showDocsDk(docs:List<Docs>)
        fun showDocsDriverCard(docs:List<Docs>)
        fun showDocsPassport(docs:List<Docs>)
        fun showDocsCardCompany(docs:List<Docs>)
        fun showDocsContractCredit(docs:List<Docs>)
        fun showDocsContractCarriage(docs:List<Docs>)
    }

    interface Presenter{

        fun onStart(view:View)

        fun onStop()

        fun onClickBack()

        fun getDocs(idVehicle:Int)
    }
}