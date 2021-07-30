package ru.smd.passnumber.ui.account.my_data

interface MyDataContract {
    interface View{

        fun showErrorMessage(message: String)

        fun showErrorInternet()

        fun toBack()

        fun showAcountFragment()

        fun exit()
    }

    interface Presenter{

        fun onStart(view:View)

        fun onStop()

        fun onClickBack()

        fun onClickSave(fio:String,phone:String,email:String,company:String)
    }
}