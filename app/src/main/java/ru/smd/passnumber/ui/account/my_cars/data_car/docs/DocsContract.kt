package ru.smd.passnumber.ui.account.my_cars.data_car.docs

import android.content.Context
import android.graphics.Bitmap
import ru.smd.passnumber.data.entities.Docs
import java.io.File

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

        fun camera()
    }

    interface Presenter{

        fun onStart(view:View)

        fun onStop()

        fun onClickBack()

        fun getDocs(idVehicle:Int)

        fun addPhoto(bitmap: Bitmap,file:File,context: Context)
        fun storeType(type:Int)

        fun storeId(idVehicle:Int)
    }
}