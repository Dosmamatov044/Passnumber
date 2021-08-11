package ru.smd.passnumber.ui.account.registration

import android.content.Context


interface RegistrationContract {
    interface View {

        fun showErrorMessage(message: String)

        fun showErrorInternet()

        fun showAccountFragment()

        fun showTimer(time: Long)

        fun activateButtons()

        fun showBlock2()

        fun exit()
    }

    interface Presenter {
        fun onStart(view: View)

        fun onStop()

        fun onClickEnter(
            androidId: String,
            code: String,
            userNameFromCheckPass: String,
            context: Context
        )

        fun startTimer()

        fun sendSms(phone: String)
    }
}