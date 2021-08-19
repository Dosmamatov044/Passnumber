package ru.smd.passnumber.ui.account.settings_notification

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.smd.passnumber.data.entities.ResponseCheckCode
import ru.smd.passnumber.data.service.*
import ru.smd.passnumber.ui.activities.main.MainActivity
import ru.smd.passnumber.ui.activities.main.MainActivity.Companion.mainCompositeDisposable
import ru.smd.passnumber.utils.toInt


/**
 * Created by Siddikov Mukhriddin on 2/13/21
 */

class SettingsNotificationViewModel @ViewModelInject constructor(
    private val repo: PassNumberRepo
) :
    ViewModel() {


    val user = MutableLiveData<ResponseCheckCode>()

    fun loadData() {
        MainActivity.handleLoad.value = true
        repo.getUser().compose(applySchedulers())
            .subscribe { response, error ->
                MainActivity.handleLoad.value=false
                handleRxErrors(error) {
                    user.value = response.data
                }

            }.also(mainCompositeDisposable::add)
    }


    fun saveNotifications( notifications_email: Boolean, notifications_push: Boolean) {
        viewModelScope.launch {
            MainActivity.handleLoad.value = true
            repo.saveNotifications(
            //    time,
                notifications_email = notifications_email.toInt(),
                notifications_push = notifications_push.toInt()
            ).compose(applySchedulers())
                .subscribe { response, error ->
                    MainActivity.handleLoad.value=false
                    handleRxErrors(error) {
                        MainActivity.handleError.value = "Настройка изменена"
                    }

                }.also(mainCompositeDisposable::add)
        }
    }


}