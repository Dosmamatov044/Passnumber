package ru.smd.passnumber.ui.account.my_cars.data_car

import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.smd.passnumber.data.service.PassNumberRepo
import ru.smd.passnumber.ui.activities.main.MainActivity
import javax.inject.Inject

class DataCarPresenter @Inject constructor(val repo: PassNumberRepo) : DataCarContract.Presenter {

    private var view: DataCarContract.View? = null

    lateinit var compositeDisposable: CompositeDisposable

    override fun onStart(view: DataCarContract.View) {
        this.view = view
        compositeDisposable = CompositeDisposable()
    }

    override fun getData(regNumber: String) {
        repo.checkPassNumber(regNumber).compose(applySchedulers()).subscribe { response, error ->
            MainActivity.handleLoad.value = false
            when {
                error == null -> {
                    val mark =response.data?.mark?:""
                    val driverName=response.data?.driverName?:""
                    view?.showData(mark,driverName,response.data?.regNumber!!)
                }
                else -> {
                    MainActivity.handleError.value = error.toString()
                }
            }
        }.also(compositeDisposable::add)
    }

    override fun onStop() {
        view = null
        compositeDisposable.dispose()
    }

    override fun onClickBack() {
        view?.toBack()
    }

    override fun onClickDataCar() {
        view?.showDataCar()
    }

    override fun onClickSaveData(mark: String, driverName: String, regNumber: String) {
        repo.addCar(
            mutableMapOf<String, String>().apply {
                this["reg_numbers"] = regNumber
                this["mark"] = mark
                this["driver_name"] = driverName
            }
        ).compose(applySchedulers())
            .subscribe { response, error ->
                MainActivity.handleLoad.value = false
                when {
                    error == null -> {
                        view?.toBack()
                    }
                    else -> {
                        MainActivity.handleError.value = error.toString()
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