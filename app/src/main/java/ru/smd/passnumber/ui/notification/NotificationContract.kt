package ru.smd.passnumber.ui.notification

interface NotificationContract {
    interface View{

       fun onBack()

    }

    interface Presenter{
        fun onStart(view:View)

        fun onStop()

        fun onClickBack()
    }
}