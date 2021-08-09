package ru.smd.passnumber.ui.account.registration



interface RegistrationContract {
    interface View{

       fun showErrorMessage(message: String)

        fun showErrorInternet()

        fun showAccountFragment()

        fun showTimer(time:Long)

        fun activateButtons()



        fun exit()
    }

    interface Presenter{
        fun onStart(view:View)

        fun onStop()

        fun onClickEnter(
            androidId: String,
            code: String,
            phone: String,
            userNameFromCheckPass: String
        )

        fun startTimer()

        fun sendSms(phone: String)
    }
}