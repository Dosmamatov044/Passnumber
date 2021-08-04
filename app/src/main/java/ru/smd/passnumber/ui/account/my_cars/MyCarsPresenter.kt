package ru.smd.passnumber.ui.account.my_cars

import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.smd.passnumber.data.service.PassNumberRepo
import ru.smd.passnumber.data.tools.PreferencesHelper
import ru.smd.passnumber.ui.activities.main.MainActivity
import javax.inject.Inject

class MyCarsPresenter @Inject constructor(val repo: PassNumberRepo,val prefs: PreferencesHelper):MyCarsContract.Presenter {

    private var view: MyCarsContract.View?=null

    lateinit var compositeDisposable: CompositeDisposable

    override fun onStart(view: MyCarsContract.View) {
        this.view=view
        compositeDisposable = CompositeDisposable()
        getMyCars()
    }

    override fun onStop() {
        view=null
        compositeDisposable.dispose()
    }

    override fun onClickBack() {
        view?.toBack()
    }

    override fun onClickAdd() {
      view?.showAddCarBlock()
    }

    override fun getMyCars() {
        MainActivity.handleLoad.postValue(true)
        repo.getCarList().compose(applySchedulers())
            .subscribe { response, error ->
                MainActivity.handleLoad.value=false
                when {
                    error == null -> {
                     if (response.data.isEmpty()){
                         view?.showEmptyList()
                     }else{
                         view?.showCarList(response.data,!prefs.getFirstAddedCar())

                     }
                    }
                    else -> {
                        view?.showErrorInternet()
                    }
                }
            }.also(compositeDisposable::add)
    }

    override fun addCar(regNumber: String, labelModel: String, nameDriver: String) {
        MainActivity.handleLoad.postValue(true)
        repo.addCar(
            mutableMapOf<String, String>().apply {
                this["reg_numbers"] = regNumber
                this["mark"] = labelModel
                this["driver_name"] = nameDriver
            }
        ).compose(applySchedulers())
            .subscribe {  response, error ->
                MainActivity.handleLoad.value=false
                when {
                    error == null -> {
                        view?.enableEdtRegNum()
                        getMyCars()
                    }
                    else -> {
                        MainActivity.handleError.value = error.toString()
//                        view?.showErrorInternet()
                    }
                }
            }.also(compositeDisposable::add)
    }

    override fun deleteCar(regNumber: Int) {
        MainActivity.handleLoad.postValue(true)
        repo.deleteCard(regNumber).compose(applySchedulers())
            .subscribe {  response, error ->
                MainActivity.handleLoad.value=false
                when (error) {
                    null -> {
                        getMyCars()
                    }
                    else -> {
                        MainActivity.handleError.value = error.toString()
            //                        view?.showErrorInternet()
                    }
                }
            }.also(compositeDisposable::add)
    }

    fun <T> applySchedulers(): SingleTransformer<T, T> {
        return SingleTransformer {
            it.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }
}