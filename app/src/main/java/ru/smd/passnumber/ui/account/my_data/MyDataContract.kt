package ru.smd.passnumber.ui.account.my_data

import android.content.Context

interface MyDataContract {
    interface View{

        fun showErrorMessage(message: String)

        fun showErrorInternet()

        fun toBack()

        fun showAcountFragment()

        fun exit()

        fun showMyData(fio: String?,phone: String?,email: String?,company: String?)
    }

    interface Presenter{

        fun onStart(view:View)

        fun onStop()

        fun onClickBack()

        fun onClickSave(fio:String,phone:String,email:String,company:String,context: Context)
    }
}