package ru.smd.passnumber.ui.account.notification

import ru.smd.passnumber.data.entities.Notification

interface NotificationContract {
    interface View{

       fun onBack()

        fun showErrorMessage(message: String)

        fun showErrorInternet()

        fun showNotifications(notifications:List<Notification>)
    }

    interface Presenter{
        fun onStart(view:View)

        fun onStop()

        fun onClickBack()

        fun sendReadNotifications(notifications: Notification)
    }
}