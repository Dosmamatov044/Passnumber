package ru.smd.passnumber.ui.checking_pass

import android.util.JsonReader
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.smd.passnumber.data.entities.PassData
import ru.smd.passnumber.data.service.PassNumberRepo
import ru.smd.passnumber.ui.activities.main.MainActivity
import ru.smd.passnumber.ui.activities.main.MainActivity.Companion.mainCompositeDisposable
import java.io.Reader


/**
 * Created by Siddikov Mukhriddin on 2/13/21
 */

class CheckingPassViewModel @ViewModelInject constructor(private val repo: PassNumberRepo) :
    ViewModel() {


    val data = MutableLiveData<PassData>()

    val check = MutableLiveData<Boolean>()

    val addCarData = MutableLiveData<Boolean>()

    var correct: Boolean = false

    fun addCar(regNumber: String, labelModel: String, nameDriver: String) {
        MainActivity.handleLoad.postValue(true)
        repo.addCar(
            mutableMapOf<String, String>().apply {
                this["reg_numbers"] = regNumber
                this["mark"] = labelModel
                this["driver_name"] = nameDriver
            }
        ).compose(applySchedulers())
            .subscribe { response, error ->
                MainActivity.handleLoad.value = false
                when {
                    error == null -> {
                        addCarData.value = true
                    }
                    else -> {
                        addCarData.value = false
                        MainActivity.handleError.value = error.toString()
                    }
                }
            }.also(mainCompositeDisposable::add)
    }

    fun checkCar(regNumber: String) {
        MainActivity.handleLoad.postValue(true)
        repo.getCarList(1).compose(applySchedulers())
            .subscribe { response, error ->
                MainActivity.handleLoad.value = false
                when {
                    error == null -> {
                        response.data.forEach {
                            if (it.regNumber?.contains(regNumber) == true) {
                                correct = true
                            }
                        }
                        check.value=correct
                    }
                    else -> {
                        MainActivity.handleError.value = error.toString()
                    }
                }
            }.also(mainCompositeDisposable::add)
    }

    fun checkPassData(number: String) {
        MainActivity.handleLoad.postValue(true)
        repo.checkPassNumber(number).compose(ru.smd.passnumber.data.service.applySchedulers()).subscribe { response, error ->
            MainActivity.handleLoad.value = false
            when {
                error == null -> {
                    data.value = response.data
                }
                else -> {
                    MainActivity.handleError.value = error.message
                }
            }
        }.also(mainCompositeDisposable::add)
    }

    fun <T> applySchedulers(): SingleTransformer<T, T> {
        return SingleTransformer {
            it.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }
}



