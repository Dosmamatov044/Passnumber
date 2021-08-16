package ru.smd.passnumber.ui.chek_pass_number

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.smd.passnumber.data.entities.PassData
import ru.smd.passnumber.data.entities.PassesData
import ru.smd.passnumber.data.service.ApiService
import ru.smd.passnumber.data.service.PassNumberRepo
import ru.smd.passnumber.data.service.applySchedulers
import ru.smd.passnumber.data.service.enqueue
import ru.smd.passnumber.ui.activities.main.MainActivity


/**
 * Created by Siddikov Mukhriddin on 2/13/21
 */

class CheckPassViewModel @ViewModelInject constructor(private val repo: PassNumberRepo) :
    ViewModel() {


    var data = MutableLiveData<PassData>()

    lateinit var compositeDisposable: CompositeDisposable

    fun composite() {
        compositeDisposable = CompositeDisposable()
    }

    fun dispose() {
        compositeDisposable.dispose()
    }

    fun checkPassData(number: String) {
        MainActivity.handleLoad.postValue(true)
        repo.checkPassNumber(number).compose(applySchedulers()).subscribe { response, error ->
            MainActivity.handleLoad.value = false
            when {
                error == null -> {
                    data.value = response.data
                    data.value=PassData(null,"","","", mutableListOf(
                        PassesData(
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null
                        )
                    ))
                }
                else -> {
                    MainActivity.handleError.value = error.toString()
                }
            }
        }.also(compositeDisposable::add)
    }

}




