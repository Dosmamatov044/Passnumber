package ru.smd.passnumber.ui.account.notification

import javax.inject.Inject

class NotificationPresenter @Inject constructor():NotificationContract.Presenter {

    private var view:NotificationContract.View?=null

    override fun onStart(view: NotificationContract.View) {
      this.view=view

    }

    override fun onStop() {
        view=null
    }

    override fun onClickBack() {
        view?.onBack()
    }
}