package ru.smd.passnumber.ui.feedback

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import ru.smd.passnumber.data.service.PassNumberRepo
import ru.smd.passnumber.data.service.applySchedulers
import ru.smd.passnumber.ui.activities.main.MainActivity
import ru.smd.passnumber.ui.activities.main.MainActivity.Companion.mainCompositeDisposable


/**
 * Created by Siddikov Mukhriddin on 2/13/21
 */

class FeedbackViewModel @ViewModelInject constructor(private val repo: PassNumberRepo) :
    ViewModel() {

    val isFeedbackSend=MutableLiveData<Boolean>()

    fun sendFeedback(phone:String,text:String){
        MainActivity.handleLoad.value = true
            repo.sendFeedback(
                mutableMapOf<String, String>().apply {
                    this["name"] = phone
                    this["text"] = text
                    this["grade"] = "5"
                }
            ).compose(applySchedulers())
                .subscribe { response, error ->
                    MainActivity.handleLoad.value = false
                    when {
                        error == null -> {
                            isFeedbackSend.value = true
                        }
                        else -> {
                            isFeedbackSend.value = false
                            MainActivity.handleError.value = error.toString()
                        }
                    }
                }.also(mainCompositeDisposable::add)
        }
}



